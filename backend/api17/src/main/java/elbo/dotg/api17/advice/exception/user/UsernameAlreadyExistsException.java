package elbo.dotg.api17.advice.exception.user;

public class UsernameAlreadyExistsException extends RuntimeException {
    //private static final String message = "해당 아이디는 이미 등록되어있습니다.";
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
