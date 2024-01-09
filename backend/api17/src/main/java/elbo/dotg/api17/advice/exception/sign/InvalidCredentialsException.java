package elbo.dotg.api17.advice.exception.sign;

public class InvalidCredentialsException extends RuntimeException {

    private static final InvalidCredentialsException INVALID_CREDENTIALS_EXCEPTION = new InvalidCredentialsException("아이디 혹은 비밀번호가 틀렸습니다.");

    public InvalidCredentialsException() {
        super(INVALID_CREDENTIALS_EXCEPTION);
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
