package elbo.dotg.api17.advice.exception.category;

public class CategoryNotFoundException extends RuntimeException{
    private static final String message = "카테고리가 존재하지 않습니다.";
    public CategoryNotFoundException() {
        super(message);
    }
    public CategoryNotFoundException(String message) {
        super(message);
    }
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
