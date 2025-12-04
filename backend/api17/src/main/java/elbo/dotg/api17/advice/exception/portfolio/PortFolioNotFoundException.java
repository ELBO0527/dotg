package elbo.dotg.api17.advice.exception.portfolio;

public class PortFolioNotFoundException extends RuntimeException{
    private static String message = "해당 정보가 존재하지 않습니다.";

    public PortFolioNotFoundException() {
        super(message);
    }

    public PortFolioNotFoundException(String message) {
        super(message);
    }

    public PortFolioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
