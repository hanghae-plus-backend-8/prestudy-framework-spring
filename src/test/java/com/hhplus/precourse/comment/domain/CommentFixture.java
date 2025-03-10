package com.hhplus.precourse.comment.domain;

import com.hhplus.precourse.common.FixtureReflectionUtils;
import com.hhplus.precourse.common.TestFixture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class CommentFixture implements TestFixture<Comment> {

    private long id = 1L;
    private long postId = 1L;
    private long userId = 1L;
    private String content = "테스트 댓글 내용";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Override
    public Comment build() {
        var entity = new Comment();
        FixtureReflectionUtils.reflect(entity, this);
        return entity;
    }
} 