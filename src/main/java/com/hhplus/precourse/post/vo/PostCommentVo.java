package com.hhplus.precourse.post.vo;

import com.hhplus.precourse.comment.domain.Comment;

import java.time.LocalDateTime;

public record PostCommentVo(
    long id,
    long userId,
    long postId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PostCommentVo from(Comment comment) {
        return new PostCommentVo(
            comment.id(),
            comment.userId(),
            comment.postId(),
            comment.content(),
            comment.createdAt(),
            comment.updatedAt()
        );
    }
}
