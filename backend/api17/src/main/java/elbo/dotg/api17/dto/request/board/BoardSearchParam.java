package elbo.dotg.api17.dto.request.board;

public record BoardSearchParam(long pageIdx, long boardIdx, String title, String content, String comment) {
}
