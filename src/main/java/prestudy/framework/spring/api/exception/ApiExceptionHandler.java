package prestudy.framework.spring.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import prestudy.framework.spring.api.controller.common.response.ApiResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ApiResponse<Void> bindException(BindException e) {
        return ApiResponse.error(
            HttpStatus.BAD_REQUEST,
            e.getBindingResult().getAllErrors().getFirst().getDefaultMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> illegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.error(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }
}
