package prestudy.framework.spring.api.service.board.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardUpdateCommand {

    private final Long id;
    private final String title;
    private final String content;
    private final String writer;
    private final String password;


    @Builder
    private BoardUpdateCommand(Long id, String title, String content, String writer, String password) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
    }

}
