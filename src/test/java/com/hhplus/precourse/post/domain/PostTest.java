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
            var author = "작성자명";
            var title = "제목";
            var content = "내용";
            var password = "비밀번호";

            // when
            var post = new Post(author, title, content, password);

            // then
            assertThat(post.author()).isEqualTo(author);
            assertThat(post.title()).isEqualTo(title);
            assertThat(post.content()).isEqualTo(content);
            assertThat(post.password()).isEqualTo(password);
        }

        @Test
        void 값검증_실패시_에외발생() {
            // given
            var author = "작성자명";
            var title = "제목";
            var content = "내용";

            // when
            var throwable = catchThrowable(() -> new Post(author, title, content, null));

            // then
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
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
            post.update(author, title, content, post.password());

            // then
            assertThat(post.author()).isEqualTo(author);
            assertThat(post.title()).isEqualTo(title);
            assertThat(post.content()).isEqualTo(content);
        }

        @Test
        void 비밀번호가_일치하지_않으면_에외발생() {
            // given
            var post = new Post("작성자명", "제목", "내용", "비밀번호");
            var author = "수정된 작성자명";
            var title = "수정된 제목";
            var content = "수정된 내용";
            var password = "다른 비밀번호";

            // when
            var throwable = catchThrowable(() -> post.update(author, title, content, password));

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessage(PASSWORD_NOT_MATCHED.message());
        }
    }

    @DisplayName("비밀번호 검증 테스트")
    @Nested
    class ValidatePasswordTest {
        @Test
        void success() {
            // given
            var post = new PostFixture().build();
            var password = post.password();

            // when
            var throwable = catchThrowable(() ->post.validatePassword(password));

            // then
            assertThat(throwable).isNull();
        }

        @Test
        void 비밀번호가_일치하지_않으면_에외발생() {
            // given
            var post = new PostFixture().build();
            var password = "다른 비밀번호";

            // when
            var throwable = catchThrowable(() -> post.validatePassword(password));

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessage(PASSWORD_NOT_MATCHED.message());
        }
    }
}