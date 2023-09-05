package elbo.dotg.api17.advice.exception;

import elbo.dotg.api17.dto.response.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static elbo.dotg.api17.dto.response.common.ApiResponse.error;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse<String> defaultException(HttpServletRequest request, Exception e) {
        return error("서버에 문제가 발생했습니다.");
    }
}
