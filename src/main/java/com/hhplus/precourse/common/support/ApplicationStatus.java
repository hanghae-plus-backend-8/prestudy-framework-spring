package com.hhplus.precourse.common.support;

public enum ApplicationStatus implements Status {
    INVALID_PARAMETER("잘못된 요청입니다."),
    POST_NOT_FOUND("게시글을 찾을 수 없습니다.")
    ;

    private final String message;

    ApplicationStatus(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
