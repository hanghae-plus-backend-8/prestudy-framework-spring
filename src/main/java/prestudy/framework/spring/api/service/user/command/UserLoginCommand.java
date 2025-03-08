package prestudy.framework.spring.api.service.user.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLoginCommand {

    private final String username;
    private final String password;

    @Builder
    private UserLoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
