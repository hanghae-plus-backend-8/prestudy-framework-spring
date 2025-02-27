package com.hhplus.precourse.common.support;

public enum ApplicationStatus implements Status {
    INVALID_PARAMETER("잘못된 요청입니다."),

    // user
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    ALREADY_EXIST_USER("이미 존재하는 사용자입니다."),
    MISMATCH_PASSWORD("비밀번호가 일치하지 않습니다."),

    // post
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
