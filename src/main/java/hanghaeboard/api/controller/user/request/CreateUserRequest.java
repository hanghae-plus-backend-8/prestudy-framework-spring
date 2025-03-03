package hanghaeboard.api.controller.user.request;

import hanghaeboard.domain.user.User;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserRequest {

    @Size(min = 4, max = 10, message = "아이디는 4글자 이상, 10글자 이하여야 합니다.")
    @Pattern(regexp = "[a-z0-9]+", message = "아이디는 소문자 영문과 숫자로만 이루어져야 합니다.")
    private String username;

    @Size(min = 8, max = 15, message = "비밀번호는 8글자 이상, 15글자 이하여야 합니다.")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "비밀번호는 소문자 혹은 대문자 영문과 숫자로만 이루어져야 합니다.")
    private String password;

    @Builder
    private CreateUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toEntity(){
        return User.builder().username(this.username).password(this.password).build();
    }
}
