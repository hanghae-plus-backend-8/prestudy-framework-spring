package prestudy.framework.spring.api.controller.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    @Builder
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(data)
            .build();
    }

    public static ApiResponse<Void> error(HttpStatus status, String message) {
        return ApiResponse.<Void>builder()
            .code(status.value())
            .message(message)
            .build();
    }
}
