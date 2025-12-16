import os
import requests
import json
from typing import List, Dict, Optional
from dotenv import load_dotenv

def _load_env() -> None:
    # 실행 위치(CWD)에 상관없이 프로젝트 루트의 .env를 읽도록 고정
    dotenv_path = os.path.join(os.path.dirname(__file__), ".env")
    load_dotenv(dotenv_path=dotenv_path, override=False)


# .env 파일 로드
_load_env()

# API 정보
NAVER_CLIENT_ID = os.getenv('NAVER_CLIENT_ID')
NAVER_CLIENT_SECRET = os.getenv('NAVER_CLIENT_SECRET')
GOOGLE_SEARCH_ENGINE_ID = (os.getenv('GOOGLE_SEARCH_ENGINE_ID') or '').strip()
GOOGLE_API_KEY = (os.getenv('GOOGLE_API_KEY') or '').strip()


def _mask_secret(value: str, keep_last: int = 4) -> str:
    if not value:
        return ""
    if len(value) <= keep_last:
        return "*" * len(value)
    return "*" * (len(value) - keep_last) + value[-keep_last:]


def _extract_google_error(payload: object) -> Dict[str, str]:
    if not isinstance(payload, dict):
        return {}
    error = payload.get("error")
    if not isinstance(error, dict):
        return {}
    first = (error.get("errors") or [{}])[0]
    if not isinstance(first, dict):
        first = {}
    return {
        "code": str(error.get("code", "")),
        "status": str(error.get("status", "")),
        "message": str(error.get("message", "")),
        "reason": str(first.get("reason", "")),
    }


def search_all(
    query: str,
    enable_blog: bool = True,
    enable_news: bool = True,
    enable_google: bool = True,
    display: int = 10
) -> Dict[str, List[Dict[str, str]]]:
    """
    네이버 블로그, 네이버 뉴스, 구글 검색을 수행하는 함수
    
    Args:
        query: 검색할 키워드
        enable_blog: 네이버 블로그 검색 활성화 여부 (기본값: True)
        enable_news: 네이버 뉴스 검색 활성화 여부 (기본값: True)
        enable_google: 구글 검색 활성화 여부 (기본값: True)
        display: 각 검색 결과 개수 (기본값: 10)
    
    Returns:
        검색 결과를 담은 딕셔너리
        {
            'blog': [{'title': ..., 'url': ..., 'content': ...}, ...],
            'news': [{'title': ..., 'url': ..., 'content': ...}, ...],
            'google': [{'title': ..., 'url': ..., 'content': ...}, ...]
        }
    """
    results = {}
    
    if enable_blog:
        results['blog'] = search_naver_blog(query, display)
    
    if enable_news:
        results['news'] = search_naver_news(query, display)
    
    if enable_google:
        results['google'] = search_google(query, display)
    
    return results


def search_many(
    queries: List[str],
    enable_blog: bool = True,
    enable_news: bool = True,
    enable_google: bool = True,
    display: int = 10,
) -> Dict[str, Dict[str, List[Dict[str, str]]]]:
    """여러 검색어를 한 번에 돌리고, 결과를 {query: results}로 반환."""
    normalized = [q.strip() for q in queries if q and q.strip()]
    out: Dict[str, Dict[str, List[Dict[str, str]]]] = {}
    for q in normalized:
        out[q] = search_all(
            q,
            enable_blog=enable_blog,
            enable_news=enable_news,
            enable_google=enable_google,
            display=display,
        )
    return out


def search_naver_blog(query: str, display: int = 10) -> List[Dict[str, str]]:
    """
    네이버 블로그 검색
    
    Args:
        query: 검색할 키워드
        display: 검색 결과 개수 (최대 100)
    
    Returns:
        검색 결과 리스트 [{'title': ..., 'url': ..., 'content': ...}, ...]
    """
    url = "https://openapi.naver.com/v1/search/blog.json"
    headers = {
        "X-Naver-Client-Id": NAVER_CLIENT_ID,
        "X-Naver-Client-Secret": NAVER_CLIENT_SECRET
    }
    params = {
        "query": query,
        "display": min(display, 100)
    }
    
    try:
        response = requests.get(url, headers=headers, params=params)
        response.raise_for_status()
        data = response.json()
        
        results = []
        for item in data.get('items', []):
            results.append({
                'title': remove_html_tags(item.get('title', '')),
                'url': item.get('link', ''),
                'content': remove_html_tags(item.get('description', ''))
            })
        
        return results
    
    except Exception as e:
        print(f"네이버 블로그 검색 오류: {e}")
        return []


def search_naver_news(query: str, display: int = 10) -> List[Dict[str, str]]:
    """
    네이버 뉴스 검색
    
    Args:
        query: 검색할 키워드
        display: 검색 결과 개수 (최대 100)
    
    Returns:
        검색 결과 리스트 [{'title': ..., 'url': ..., 'content': ...}, ...]
    """
    url = "https://openapi.naver.com/v1/search/news.json"
    headers = {
        "X-Naver-Client-Id": NAVER_CLIENT_ID,
        "X-Naver-Client-Secret": NAVER_CLIENT_SECRET
    }
    params = {
        "query": query,
        "display": min(display, 100)
    }
    
    try:
        response = requests.get(url, headers=headers, params=params)
        response.raise_for_status()
        data = response.json()
        
        results = []
        for item in data.get('items', []):
            results.append({
                'title': remove_html_tags(item.get('title', '')),
                'url': item.get('link', ''),
                'content': remove_html_tags(item.get('description', ''))
            })
        
        return results
    
    except Exception as e:
        print(f"네이버 뉴스 검색 오류: {e}")
        return []


def search_google(query: str, num: int = 10) -> List[Dict[str, str]]:
    """
    구글 검색 (Custom Search JSON API 사용)
    
    Args:
        query: 검색할 키워드
        num: 검색 결과 개수 (최대 10)
    
    Returns:
        검색 결과 리스트 [{'title': ..., 'url': ..., 'content': ...}, ...]
    """
    if not GOOGLE_API_KEY:
        print("GOOGLE_API_KEY가 설정되지 않았습니다.")
        return []

    if not GOOGLE_SEARCH_ENGINE_ID:
        print("GOOGLE_SEARCH_ENGINE_ID(cx)가 설정되지 않았습니다.")
        return []
    
    url = "https://www.googleapis.com/customsearch/v1"
    params = {
        "key": GOOGLE_API_KEY,
        "cx": GOOGLE_SEARCH_ENGINE_ID,
        "q": query,
        "num": min(num, 10)
    }
    
    try:
        response = requests.get(url, params=params, timeout=15)
        response.raise_for_status()
        data = response.json()
        
        results = []
        for item in data.get('items', []):
            results.append({
                'title': item.get('title', ''),
                'url': item.get('link', ''),
                'content': item.get('snippet', '')
            })
        
        return results
    
    except requests.exceptions.HTTPError as e:
        details: Dict[str, str] = {}
        try:
            details = _extract_google_error(response.json())
        except Exception:
            details = {}

        print(
            "구글 검색 HTTP 오류: "
            f"status={getattr(response, 'status_code', '')} "
            f"code={details.get('code', '')} statusText={details.get('status', '')} "
            f"reason={details.get('reason', '')} message={details.get('message', '')}"
        )
        print(
            "구글 설정 확인: "
            f"cx={GOOGLE_SEARCH_ENGINE_ID} api_key={_mask_secret(GOOGLE_API_KEY)}"
        )
        if details.get("reason") == "notFound" or "Requested entity was not found" in (details.get("message") or ""):
            print(
                "힌트: 이 404(notFound)는 보통 cx(Search Engine ID)가 실제로 존재하지 않거나, "
                "다른 엔진 ID를 넣었을 때 나옵니다. PSE 콘솔의 'Search engine ID' 또는 임베드 코드의 cx 값을 "
                "그대로(콜론 포함이면 콜론까지) 복사해서 GOOGLE_SEARCH_ENGINE_ID에 넣어보세요."
            )
        return []
    except Exception as e:
        print(f"구글 검색 오류: {e}")
        return []


def remove_html_tags(text: str) -> str:
    """
    HTML 태그 제거 (네이버 API는 <b>, </b> 태그를 포함)
    
    Args:
        text: HTML 태그가 포함된 텍스트
    
    Returns:
        HTML 태그가 제거된 텍스트
    """
    import re
    clean = re.compile('<.*?>')
    return re.sub(clean, '', text)


def save_results_json(data: object, output_path: str) -> str:
    os.makedirs(os.path.dirname(output_path) or ".", exist_ok=True)
    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    return output_path


# 사용 예시
if __name__ == "__main__":
    # 검색 키워드
    keyword = "오라클, 래리 엘리슨"
    
    # 모든 검색 실행 (기본값)
    print("=== 모든 검색 실행 ===")
    keywords = [k.strip() for k in keyword.split(",") if k.strip()]
    if len(keywords) <= 1:
        all_results = search_all(keyword.strip())
    else:
        all_results = search_many(keywords)

    print(json.dumps(all_results, ensure_ascii=False, indent=2))

    output_path = os.path.join(os.path.dirname(__file__), "search_results.json")
    # 블로그만 / 뉴스+구글도 동일한 키워드 분해 규칙을 사용
    if len(keywords) <= 1:
        blog_only = search_all(keyword.strip(), enable_news=False, enable_google=False)
        news_google = search_all(keyword.strip(), enable_blog=False)
    else:
        blog_only = search_many(keywords, enable_news=False, enable_google=False)
        news_google = search_many(keywords, enable_blog=False)

    saved_payload = {
        "keywords": keywords if keywords else [keyword.strip()],
        "all": all_results,
        "blog_only": blog_only,
        "news_google": news_google,
    }

    saved_path = save_results_json(saved_payload, output_path)
    print(f"\n[저장 완료] {saved_path}\n")
    
    print("\n" + "="*50 + "\n")
    
    # 블로그만 검색
    print("=== 블로그만 검색 ===")
    print(json.dumps(blog_only, ensure_ascii=False, indent=2))
    
    print("\n" + "="*50 + "\n")
    
    # 뉴스와 구글만 검색
    print("=== 뉴스와 구글만 검색 ===")
    print(json.dumps(news_google, ensure_ascii=False, indent=2))
