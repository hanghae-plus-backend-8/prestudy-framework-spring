package com.hhplus.precourse.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.hhplus.precourse.common.exception.DomainException;

class CommentTest {
    private final CommentValidator validator = new TestValidator();

    @Nested
    @DisplayName("댓글 생성 테스트")
    class ConstructorTest {
        @Test
        void success() {
            // given when
            var comment = new Comment(
                1L,
                1L,
                "test-content",
                validator
            );

            // then
            assertThat(comment.id()).isNull();
            assertThat(comment.userId()).isEqualTo(1L);
            assertThat(comment.postId()).isEqualTo(1L);
            assertThat(comment.content()).isEqualTo("test-content");
        }
    }

    @Nested
    @DisplayName("댓글 내용 검증 테스트")
    class ValidateContent {
        @Test
        void success() {
            // given
            var comment = new CommentFixture()
                .setContent("test-content")
                .build();

            // when
            var throwable = catchThrowable(comment::validateContent);

            // then
            assertThat(throwable).isNull();
        }

        @Test
        void 댓글_내용_길이_검증_실패_케이스() {
            // given
            var comment = new CommentFixture()
                .setContent("a".repeat(201))
                .build();

            // when
            var throwable = catchThrowable(comment::validateContent);

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessageContaining("댓글 내용은 200자를 초과할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("댓글 수정 테스트")
    class UpdateTest {
        @Test
        void success() {
            // given
            var comment = new CommentFixture()
                .setContent("test-content")
                .build();

            // when
            comment.update("updated-content");

            // then
            assertThat(comment.content()).isEqualTo("updated-content");
        }
    }
    

    private static class TestValidator implements CommentValidator {
        @Override
        public void validateCreation(Comment comment) {
            // ignore
        }

        @Override
        public void validateModification(Comment comment) {
            // ignore
        }
    }
}