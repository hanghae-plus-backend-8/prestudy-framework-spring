package com.hhplus.precourse.user.domain;

import com.hhplus.precourse.common.FixtureReflectionUtils;
import com.hhplus.precourse.common.TestFixture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class UserFixture implements TestFixture<User> {
    private long id = 1L;
    private String name = "test1234";
    private String password = "testPassword";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


    @Override
    public User build() {
        var entity = new User();
        FixtureReflectionUtils.reflect(entity, this);
        return entity;
    }
}
