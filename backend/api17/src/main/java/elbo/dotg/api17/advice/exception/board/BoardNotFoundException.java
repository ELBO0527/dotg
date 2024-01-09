package elbo.dotg.api17.advice.exception.board;

public class BoardNotFoundException extends RuntimeException{
    private static String message = "해당 게시판이 존재하지 않습니다.";

    public BoardNotFoundException() {
        super(message);
    }

    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
