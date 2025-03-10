package com.hhplus.precourse.comment.vo;

import java.time.LocalDateTime;

import com.hhplus.precourse.comment.domain.Comment;

public record CommentVo(
    long id,
    long userId,
    long postId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CommentVo from(Comment comment) {
        return new CommentVo(
            comment.id(),
            comment.userId(),
            comment.postId(),
            comment.content(),
            comment.createdAt(),
            comment.updatedAt()
        );
    }
}
