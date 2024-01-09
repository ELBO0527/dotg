package elbo.dotg.api17.dto.response.board;

import elbo.dotg.api17.domain.board.Board;

import java.util.List;

public record BoardResponse(long id, String title, String content, List<String> attachments, long viewCount,
                            String categoryName, String userName) {
    public static BoardResponse from(final Board board) {
        return new BoardResponse(board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getAttachments(),
                board.getViewCount(),
                board.getCategory() != null ? board.getCategory().getName() : "카테고리 미지정",
                board.getUser() != null ? board.getUser().getName() : "익명");
    }

    public Board toEntity() {
        //return new Board(null,this.title,this.content,this.viewCount,null,this.attachments,this.toEntity().getCategory(), this.toEntity().getUser());
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .viewCount(this.viewCount)
                .attachments(this.attachments)
                .category(this.toEntity().getCategory())
                .user(this.toEntity().getUser())
                .build();
    }
}
