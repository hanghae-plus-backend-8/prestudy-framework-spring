package com.hhplus.precourse.comment.domain;

import static com.hhplus.precourse.comment.domain.Comment.CommentErrorStatus.INVALID_PARAMETER;
import com.hhplus.precourse.common.entity.BaseEntity;
import com.hhplus.precourse.common.exception.DomainException;
import com.hhplus.precourse.common.support.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "comments")
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long postId;

    private long userId;

    private String content;

    public Comment(long postId,
                   long userId,
                   String content,
                   CommentValidator commentValidator) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        commentValidator.validateCreation(this);
    }

    public void validateValues() {
        validateContent();
    }

    public void validateContent() {
        if (content == null || content.strip().isBlank()) {
            throw new DomainException(INVALID_PARAMETER, "댓글 내용은 필수입니다.");
        }

        if (content.length() > 200) {
            throw new DomainException(INVALID_PARAMETER, "댓글 내용은 200자를 초과할 수 없습니다.");
        }
    }

    public void update(String content) {
        this.content = content;
        validateValues();
    }

    public boolean isAuthor(long userId) {
        return this.userId == userId;
    }


    enum CommentErrorStatus implements Status {
        INVALID_PARAMETER("잘못된 요청입니다.");

        private final String message;

        CommentErrorStatus(String message) {
            this.message = message;
        }

        @Override
        public String message() {
            return message;
        }
    }
} 