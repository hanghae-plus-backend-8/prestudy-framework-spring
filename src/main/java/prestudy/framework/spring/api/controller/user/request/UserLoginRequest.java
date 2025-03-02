package prestudy.framework.spring.api.controller.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.service.user.command.UserLoginCommand;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "유저명은 필수 값 입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;

    @Builder
    private UserLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserLoginCommand toCommand() {
        return UserLoginCommand.builder()
            .username(username)
            .password(password)
            .build();
    }
}
