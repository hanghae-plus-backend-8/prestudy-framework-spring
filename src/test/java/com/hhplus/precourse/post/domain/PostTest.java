package com.hhplus.precourse.post.domain;

import com.hhplus.precourse.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.hhplus.precourse.post.domain.Post.PostErrorStatus.PASSWORD_NOT_MATCHED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PostTest {

    @DisplayName("생성자 테스트")
    @Nested
    class ConstructorTest {
        @Test
        void success() {
            // given
            var userId = 1L;
            var author = "작성자명";
            var title = "제목";
            var content = "내용";

            // when
            var post = new Post(userId, author, title, content);

            // then
            assertThat(post.author()).isEqualTo(author);
            assertThat(post.title()).isEqualTo(title);
            assertThat(post.content()).isEqualTo(content);
        }

        @Test
        void 값검증_실패시_에외발생() {
            // given
            var userId = 1L;
            var author = "작성자명";
            var title = "제목";

            // when
            var throwable = catchThrowable(() -> new Post(userId, author, title, null));

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessage("내용은 필수 값입니다.");
        }
    }

    @DisplayName("수정 테스트")
    @Nested
    class UpdateTest {
        @Test
        void success() {
            // given
            var post = new PostFixture().build();
            var author = "수정된 작성자명";
            var title = "수정된 제목";
            var content = "수정된 내용";

            // when
            post.update(author, title, content);

            // then
            assertThat(post.author()).isEqualTo(author);
            assertThat(post.title()).isEqualTo(title);
            assertThat(post.content()).isEqualTo(content);
        }
    }
}