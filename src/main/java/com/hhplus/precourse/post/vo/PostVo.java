package com.hhplus.precourse.post.vo;

import com.hhplus.precourse.post.domain.Post;

import java.time.LocalDateTime;

public record PostVo(
    long id,
    String author,
    String title,
    String content,
    String password,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PostVo from(Post post) {
        return new PostVo(
            post.id(),
            post.author(),
            post.title(),
            post.content(),
            post.password(),
            post.createdAt(),
            post.updatedAt()
        );
    }
}
