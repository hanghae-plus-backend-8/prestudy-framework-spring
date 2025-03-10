package com.hhplus.precourse.user.vo;

import com.hhplus.precourse.user.domain.User;

public record UserVo(
    long id,
    String name,
    User.RoleType role
) {
    public static UserVo from(User user) {
        return new UserVo(
            user.id(),
            user.name(),
            user.role()
        );
    }
}