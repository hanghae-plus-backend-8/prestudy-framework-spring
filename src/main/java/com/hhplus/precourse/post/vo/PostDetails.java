package com.hhplus.precourse.post.vo;

import com.hhplus.precourse.comment.domain.Comment;
import com.hhplus.precourse.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetails(
    long id,
    long userId,
    String author,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<PostCommentVo> comments
) {
    public static PostDetails of(Post post, List<Comment> comments) {
        return new PostDetails(
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
