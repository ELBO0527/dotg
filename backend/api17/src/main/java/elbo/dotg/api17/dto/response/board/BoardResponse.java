package elbo.dotg.api17.dto.response.board;

import elbo.dotg.api17.domain.board.Board;

import java.util.List;

public record FindBoardsResponse(String title, String content, List<String> attachments, long viewCount, String categoryName, String userName) {
    public static FindBoardsResponse from(final Board board){
        return new FindBoardsResponse(board.getTitle(),
                                      board.getContent(),
                                      board.getAttachments(),
                                      board.getViewCount(),
                                      board.getCategory().getName(),
                                      board.getUser().getName());
    }
}
