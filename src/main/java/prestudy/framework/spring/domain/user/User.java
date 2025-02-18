package prestudy.framework.spring.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    public static final int USERNAME_MIN_LENGTH = 4;
    public static final int USERNAME_MAX_LENGTH = 10;
    public static final Pattern USERNAME_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*\\d)[a-z0-9]*$");
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 15;
    public static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]*$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private User(String username, String password) {
        validationUsername(username);
        validationPassword(password);

        this.username = username;
        this.password = password;
    }

    private void validationUsername(String username) {
        if (username.length() < USERNAME_MIN_LENGTH) {
            throw new IllegalArgumentException("유저명은 최소 4자 이상이여야 합니다.");
        }

        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new IllegalArgumentException("유저명은 최대 10자 이상이여야 합니다.");
        }

        if (!USERNAME_REGEX.matcher(username).matches()) {
            throw new IllegalArgumentException("유저명은 알파벳 소문자와 숫자로 구성되어야 합니다.");
        }
    }

    private void validationPassword(String password) {
        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이여야 합니다.");
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 최대 15자 이상이여야 합니다.");
        }

        if (!PASSWORD_REGEX.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호는 알파벳 대소문자와 숫자로 구성되어야 합니다.");
        }
    }

    public static User of(String username, String password) {
        return new User(username, password);
    }

    public boolean isNotEqualPassword(String password) {
        return !this.password.equals(password);
    }
}
