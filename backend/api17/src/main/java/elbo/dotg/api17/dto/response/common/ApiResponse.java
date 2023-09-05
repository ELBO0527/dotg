package elbo.dotg.api17.dto.response.common;

import elbo.dotg.api17.common.ApiStatus;

public record ApiResponse<T>(ApiStatus status,
                          String message,
                          T data) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiStatus.SUCCESS, null, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ApiStatus.ERROR, message, null);
    }
}
