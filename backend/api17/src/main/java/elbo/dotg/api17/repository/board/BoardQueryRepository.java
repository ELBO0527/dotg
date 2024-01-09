package elbo.dotg.api17.repository.board;

import com.querydsl.core.types.Projections;

import com.querydsl.jpa.impl.JPAQueryFactory;
import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.dto.request.board.BoardSearchParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.constantAs;
import static elbo.dotg.api17.domain.board.QBoard.board;

@RequiredArgsConstructor
@Repository
public class BoardQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Board> findBySearchParam(String param){
        return queryFactory.selectFrom(board)
                .where(board.title.eq(param))
                .fetch();
    }

    /*
    * DTO 사용
    * 페이징
    * 조회쿼리
    * */
    public List<BoardSearchParam> findByPagingSearchParam(long boardIdx, long pageIdx){
        return queryFactory.select(Projections.fields(BoardSearchParam.class,
                        constantAs(boardIdx, board.id)
                ))
                .from(board)
                .where(board.id.eq(boardIdx))
                .offset(pageIdx)
                .limit(10)
                .fetch();
    }
}
