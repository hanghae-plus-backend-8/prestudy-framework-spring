package prestudy.framework.spring.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import prestudy.framework.spring.api.controller.common.response.ApiResponse;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> bindException(BindException e) {
        return ApiResponse.error(
            HttpStatus.BAD_REQUEST,
            e.getBindingResult().getAllErrors().getFirst().getDefaultMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> illegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.error(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> tokenException(AuthenticationException e) {
        log.error("TokenException", e);
        return ApiResponse.error(
            HttpStatus.BAD_REQUEST,
            "토큰이 유효하지 않습니다."
        );
    }
}
