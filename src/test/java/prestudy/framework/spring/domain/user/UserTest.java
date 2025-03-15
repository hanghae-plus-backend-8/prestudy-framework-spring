package prestudy.framework.spring.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @DisplayName("유저명의 최소 길이는 4이여야 한다.")
    @Test
    void validateUsernameMinLength() {
        // given
        String username = "123";
        String password = "<PASSWORD>";

        // when & then
        assertThatThrownBy(() -> User.ofUser(username, password))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유저명은 최소 4자 이상이여야 합니다.");
    }

    @DisplayName("유저명의 최대 길이는 10이여야 한다.")
    @Test
    void validateUsernameMaxLength() {
        // given
        String username = "12345678901";
        String password = "<PASSWORD>";

        // when & then
        assertThatThrownBy(() -> User.ofUser(username, password))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유저명은 최대 10자 이상이여야 합니다.");
    }

    @DisplayName("유저명은 알파벳 소문자와 숫자로 구성되어야 한다.")
    @ParameterizedTest
    @ValueSource(strings = {"123456", "abcdef", "ABCDEF", "A12a", "아이디123a"})
    void validateUsernameNoneMatch(String username) {
        // given
        String password = "<PASSWORD>";

        // when & then
        assertThatThrownBy(() -> User.ofUser(username, password))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유저명은 알파벳 소문자와 숫자로 구성되어야 합니다.");
    }

    @DisplayName("비밀번호의 최소 길이는 8이여야 한다.")
    @Test
    void validatePasswordMinLength() {
        // given
        String username = "123abc";
        String password = "1234567";

        // when & then
        assertThatThrownBy(() -> User.ofUser(username, password))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호는 최소 8자 이상이여야 합니다.");
    }

    @DisplayName("비밀번호의 최대 길이는 15이여야 한다.")
    @Test
    void validatePasswordMaxLength() {
        // given
        String username = "123abc";
        String password = "1234567890123456";

        // when & then
        assertThatThrownBy(() -> User.ofUser(username, password))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호는 최대 15자 이상이여야 합니다.");
    }

    @DisplayName("비밀번호는 알파벳 대소문자와 특수문자, 숫자로 구성되어야 한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "abcdefghij", "ABCDEFGHIJ", "123456abc", "패스워드입력해주세", "abcdefABCD", "ABCD1234"})
    void validatePasswordNoneMatch(String password) {
        // given
        String username = "123abc";

        // when & then
        assertThatThrownBy(() -> User.ofUser(username, password))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호는 알파벳 대소문자와 특수문자, 숫자로 구성되어야 합니다.");
    }

    @DisplayName("유저명과 비밀번호가 올바르면 유저를 생성한다.")
    @Test
    void createUserWithValidUsernameAndPassword() {
        // given
        String username = "123abc";
        String password = "Password12!";

        // when
        User user = User.ofUser(username, password);

        // then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @DisplayName("회원가입시 기본 권한은 일반 유저의 권한으로 생성된다.")
    @Test
    void createUserWithDefaultRole() {
        // given
        String username = "123abc";
        String password = "Password12!";

        // when
        User user = User.ofUser(username, password);

        // then
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
    }

    @DisplayName("유저가 관리자인지 판별한다.")
    @Test
    void isAdmin() {
        // given
        String username = "123abc";
        String password = "Password12!";

        // when
        User user = User.ofUser(username, password);
        User admin = User.ofAdmin(username, password);

        // then
        assertThat(user.isAdmin()).isFalse();
        assertThat(admin.isAdmin()).isTrue();
    }
}