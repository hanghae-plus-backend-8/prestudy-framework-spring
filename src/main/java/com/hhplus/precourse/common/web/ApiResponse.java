package com.hhplus.precourse.common.web;

import com.hhplus.precourse.common.support.Status;

public record ApiResponse<T>(
    Status status,
    String message,
    T data
) {
    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
            ApiResponseStatus.SUCCESS,
            ApiResponseStatus.SUCCESS.message(),
            data
        );
    }

    public static <T> ApiResponse<T> fail(Status status,
                                          String message) {
        return fail(
            status,
            message,
            null
        );
    }

    public static <T> ApiResponse<T> fail(String message) {
        return fail(
            ApiResponseStatus.FAILURE,
            message,
            null
        );
    }

    public static <T> ApiResponse<T> fail(Status status,
                                          String message,
                                          T data) {
        return new ApiResponse<>(
            status,
            message,
            data
        );
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(
            message,
            null
        );
    }

    public static <T> ApiResponse<T> error(Status status,
                                           String message) {
        return new ApiResponse<>(
            status,
            message,
            null
        );
    }

    public static <T> ApiResponse<T> error(String message,
                                           T errors) {
        return new ApiResponse<>(
            ApiResponseStatus.ERROR,
            message,
            errors
        );
    }

    public static <T> ApiResponse<T> custom(Status status,
                                            String message,
                                            T data) {
        return new ApiResponse<>(
            status,
            message,
            data
        );
    }

    enum ApiResponseStatus implements Status {
        SUCCESS("성공"),
        FAILURE( "요청에 실패하였습니다."),
        ERROR( "에러가 발생하였습니다.");

        private final String message;

        ApiResponseStatus(String message) {
            this.message = message;
        }

        @Override
        public String message() {
            return message;
        }
    }
}

