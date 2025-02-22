package prestudy.framework.spring.api.service.board.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDeleteCommand {

    private final Long id;
    private final String password;

    @Builder
    private BoardDeleteCommand(Long id, String password) {
        this.id = id;
        this.password = password;
    }
}
