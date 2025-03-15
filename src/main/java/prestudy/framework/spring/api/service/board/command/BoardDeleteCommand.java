package prestudy.framework.spring.api.service.board.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDeleteCommand {

    private final Long id;

    @Builder
    private BoardDeleteCommand(Long id) {
        this.id = id;
    }

    public static BoardDeleteCommand of(Long id) {
        return new BoardDeleteCommand(id);
    }
}
