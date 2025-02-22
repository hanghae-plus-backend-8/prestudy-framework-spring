package prestudy.framework.spring.api.service.board.command;

import lombok.Builder;
import lombok.Getter;
import prestudy.framework.spring.domain.board.Board;

@Getter
public class BoardCreateCommand {

    private final String title;
    private final String content;
    private final String writer;
    private final String password;

    @Builder
    private BoardCreateCommand(String title, String content, String writer, String password) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
    }

    public Board toEntity() {
        return Board.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .password(password)
            .build();
    }
}
