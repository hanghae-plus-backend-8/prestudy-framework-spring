package com.hhplus.precourse.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.hhplus.precourse.common.exception.DomainException;

class UserTest {

    @Nested
    @DisplayName("이름 검증 테스트")
    class ValidateNameTest {
        @Test
        void success() {
            // given
            var user = new UserFixture()
                .setName("test1234")
                .build();

            // when
            var throwable = catchThrowable(user::validateName);

            // then
            assertThat(throwable).isNull();
        }

        @Test
        void 이름_길이_검증_실패_케이스() {
            // given
            var user = new UserFixture()
                .setName("11")
                .build();

            // when
            var throwable = catchThrowable(user::validateName);

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessageContaining("이름은 4자 이상 10자 이하로 입력해주세요.");
        }

        @Test
        void 이름_패턴_검증_실패_케이스() {
            // given
            var user = new UserFixture()
                .setName("test!@#")
                .build();

            // when
            var throwable = catchThrowable(user::validateName);

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessageContaining("이름은 알파벳 소문자, 숫자로 구성되어야합니다.");
        }
    }

    @Nested
    @DisplayName("비밀번호 검증 테스트")
    class ValidatePasswordTest {
        @Test
        void success() {
            // given
            var user = new UserFixture()
                .setPassword("test1234")
                .build();

            // when
            var throwable = catchThrowable(user::validatePassword);

            // then
            assertThat(throwable).isNull();
        }

        @Test
        void 비밀번호_길이_검증_실패_케이스() {
            // given
            var user = new UserFixture()
                .setPassword("1234567")
                .build();

            // when
            var throwable = catchThrowable(user::validatePassword);

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessageContaining("비밀번호는 8자 이상 15자 이하로 입력해주세요.");
        }


        @Test
        void 비밀번호_패턴_검증_실패_케이스() {
            // given
            var user = new UserFixture()
                .setPassword("test1234!$")
                .build();

            // when
            var throwable = catchThrowable(user::validatePassword);

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessageContaining("비밀번호는 알파벳 대소문자, 숫자로 구성되어야합니다.");
        }
    }

    @Nested
    @DisplayName("비밀번호 일치 테스트")
    class MatchPasswordTest {
        @Test
        void success() {
            // given
            var user = new UserFixture()
                .setPassword("test1234")
                .build();

            // when
            var isMatch = user.matchPassword(user.password());

            // then
            assertThat(isMatch).isTrue();
        }

        @Test
        void 비밀번호_불일치_케이스() {
            // given
            var user = new UserFixture()
                .setPassword("test1234")
                .build();

            // when
            var isMatch = user.matchPassword("test12345");

            // then
            assertThat(isMatch).isFalse();
        }
    }
}
