package elbo.dotg.api17.advice.exception.sign;

public class CustomAuthenticationException extends RuntimeException {

    private static final CustomAuthenticationException AUTHENTICATION_EXCEPTION = new CustomAuthenticationException("아이디 혹은 비밀번호가 틀렸습니다.");

    public CustomAuthenticationException() {
        super(AUTHENTICATION_EXCEPTION);
    }

    public CustomAuthenticationException(String message) {
        super(message);
    }
}
