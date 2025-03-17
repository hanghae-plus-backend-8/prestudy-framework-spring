package com.hhplus.precourse.post.domain;

import com.hhplus.precourse.common.entity.BaseEntity;
import com.hhplus.precourse.common.exception.DomainException;
import com.hhplus.precourse.common.support.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

import static com.hhplus.precourse.post.domain.Post.PostErrorStatus.INVALID_PARAMETER;
import static com.hhplus.precourse.post.domain.Post.PostErrorStatus.PASSWORD_NOT_MATCHED;

@Entity
@Table(name = "posts")
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    private String author;

    private String title;

    private String content;

    public Post(long userId,
                String author,
                String title,
                String content) {
        this.userId = userId;
        this.author = author;
        this.title = title;
        this.content = content;
        validateValues();
    }

    void validateValues() {
        if (userId < 0) {
            throw new DomainException(INVALID_PARAMETER, "userId는 양수여야만 합니다.");
        }

        if (author == null || author.strip().isBlank()) {
            throw new DomainException(INVALID_PARAMETER, "작성자는 필수 값입니다.");
        }

        if (title == null || title.isBlank()) {
            throw new DomainException(INVALID_PARAMETER, "제목은 필수 값입니다.");
        }

        if (content == null || content.isBlank()) {
            throw new DomainException(INVALID_PARAMETER, "내용은 필수 값입니다.");
        }

        if (author.length() >= 20) {
            throw new DomainException(INVALID_PARAMETER, "작성자는 20자 이하로 입력해주세요.");
        }

        if (title.length() >= 50) {
            throw new DomainException(INVALID_PARAMETER, "제목은 50자 이하로 입력해주세요.");
        }

        if (content.length() >= 1000) {
            throw new DomainException(INVALID_PARAMETER, "내용은 1000자 이하로 입력해주세요.");
        }
    }

    public void update(String author,
                       String title,
                       String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        validateValues();
    }

    public boolean isAuthor(long userId) {
        return this.userId == userId;
    }

    @Getter
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    enum PostErrorStatus implements Status {
        INVALID_PARAMETER("잘못된 요청입니다."),
        PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다.");

        private final String message;
    }
}
