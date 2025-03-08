package prestudy.framework.spring.api.service.user.command;

import lombok.Builder;
import lombok.Getter;
import prestudy.framework.spring.domain.user.User;

@Getter
public class UserCreateCommand {

    private final String username;
    private final String password;

    @Builder
    private UserCreateCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toEntity() {
        return User.of(username, password);
    }
}
