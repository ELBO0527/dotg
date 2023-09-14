package elbo.dotg.api17.advice.exception;

import elbo.dotg.api17.advice.exception.sign.CustomAuthenticationException;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static elbo.dotg.api17.dto.response.common.ApiResponse.error;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse<String> defaultRuntimeException(final RuntimeException e) {
        return error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse<String> defaultException(HttpServletRequest request, Exception e) {
        return error(e.getLocalizedMessage());
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<String> customAuthenticationException(HttpServletRequest request, CustomAuthenticationException e) {
        return error(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ApiResponse<String> authenticationException(HttpServletRequest request, AuthenticationException e) {
        return error(e.getMessage());
    }
}
