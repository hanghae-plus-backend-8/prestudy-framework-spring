package com.hhplus.precourse.post.vo;

import com.hhplus.precourse.post.domain.Post;

import java.time.LocalDateTime;

public record PostVo(
    long id,
    long userId,
    String author,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PostVo from(Post post) {
        return new PostVo(
            post.id(),
            post.userId(),
            post.author(),
            post.title(),
            post.content(),
            post.createdAt(),
            post.updatedAt()
        );
    }
}
