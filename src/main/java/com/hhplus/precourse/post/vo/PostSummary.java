package com.hhplus.precourse.post.vo;

import com.hhplus.precourse.comment.domain.Comment;
import com.hhplus.precourse.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostSummary(
    long id,
    long userId,
    String author,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<PostCommentVo> comments
) {
    public static PostSummary of(Post post, List<Comment> comments) {
        return new PostSummary(
            post.id(),
            post.userId(),
            post.author(),
            post.title(),
            post.content(),
            post.createdAt(),
            post.updatedAt(),
            comments.stream()
                .map(PostCommentVo::from)
                .toList()
        );
    }
}
