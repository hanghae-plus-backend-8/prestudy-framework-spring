package prestudy.framework.spring.api.controller.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.service.user.command.UserCreateCommand;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "유저명은 필수 값 입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;

    @Builder
    private UserCreateRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserCreateCommand toCommand() {
        return UserCreateCommand.builder()
            .username(username)
            .password(password)
            .build();
    }
}
