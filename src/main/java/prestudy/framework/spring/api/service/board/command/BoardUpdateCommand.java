package prestudy.framework.spring.api.service.board.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardUpdateCommand {

    private final Long id;
    private final String title;
    private final String content;

    @Builder
    private BoardUpdateCommand(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

}
