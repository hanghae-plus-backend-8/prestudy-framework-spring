package com.hhplus.precourse.post.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
}