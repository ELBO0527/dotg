from __future__ import annotations

import os
from typing import Any, Dict, List, Optional

from fastapi import APIRouter, Depends, FastAPI, Header, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

# 재사용: main.py 안에 이미 .env 로드 + 각 검색 함수 구현됨
from main import search_all, search_many


app = FastAPI(title="DOTG Search API", version="1.0.0")


# 간단 인증(고정 헤더 기반)
# n8n HTTP Request 노드에서 헤더로 아래 키/값을 반드시 보내야 /search가 동작합니다.
API_KEY_HEADER_NAME = "X-DOTG-KEY"
API_KEY_HEADER_VALUE = "qwer1234"


def require_api_key(api_key: str | None = Header(default=None, alias=API_KEY_HEADER_NAME)) -> None:
    if api_key != API_KEY_HEADER_VALUE:
        raise HTTPException(status_code=401, detail="Unauthorized")


# 앞으로 추가될(보호가 필요한) 엔드포인트는 이 라우터에 붙이면
# API 키 헤더 체크가 자동으로 적용됩니다.
protected_router = APIRouter(dependencies=[Depends(require_api_key)])

# CORS 설정
# - 브라우저(프론트/웹앱)에서 호출하면 CORS가 필요합니다.
# - n8n이 서버에서 직접 호출하는 경우(일반 HTTP Request 노드)는 보통 CORS 영향이 없습니다.
# 환경변수 예:
#   CORS_ALLOW_ORIGINS=*                    (전체 허용)
#   CORS_ALLOW_ORIGINS=http://localhost:5678,http://localhost:3000
_cors_origins_raw = (os.getenv("CORS_ALLOW_ORIGINS") or "*").strip()
_cors_origins = [o.strip() for o in _cors_origins_raw.split(",") if o.strip()]
app.add_middleware(
    CORSMiddleware,
    allow_origins=_cors_origins or ["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"]
)


class SearchRequest(BaseModel):
    # n8n에서 쓰기 편하게 단일/복수 둘 다 지원
    query: Optional[str] = Field(default=None, description="단일 검색어")
    queries: Optional[List[str]] = Field(default=None, description="복수 검색어 리스트")

    enable_blog: bool = True
    enable_news: bool = True
    enable_google: bool = True

    display: int = Field(default=10, ge=1, le=100, description="네이버 최대 100, 구글은 내부적으로 10으로 제한")


class SearchResponse(BaseModel):
    keywords: List[str]
    results: Dict[str, Dict[str, List[Dict[str, str]]]]


@app.get("/health")
def health() -> Dict[str, str]:
    return {"status": "ok"}


@protected_router.post("/search", response_model=SearchResponse)
def search(req: SearchRequest) -> SearchResponse:
    keywords: List[str] = []

    if req.queries is not None:
        keywords = [q.strip() for q in req.queries if q and q.strip()]
    elif req.query is not None:
        # 콤마로 여러개 넣는 것도 허용 (n8n 텍스트 한 칸에 넣기 편함)
        keywords = [q.strip() for q in req.query.split(",") if q.strip()]

    if not keywords:
        raise HTTPException(status_code=400, detail="'query' 또는 'queries' 중 하나는 필수입니다.")

    # 단일/복수 결과 포맷을 n8n에서 다루기 편하게 항상 {keyword: result}로 통일
    if len(keywords) == 1:
        results: Dict[str, Dict[str, List[Dict[str, str]]]] = {
            keywords[0]: search_all(
                keywords[0],
                enable_blog=req.enable_blog,
                enable_news=req.enable_news,
                enable_google=req.enable_google,
                display=req.display,
            )
        }
    else:
        results = search_many(
            keywords,
            enable_blog=req.enable_blog,
            enable_news=req.enable_news,
            enable_google=req.enable_google,
            display=req.display,
        )

    return SearchResponse(keywords=keywords, results=results)


app.include_router(protected_router)
