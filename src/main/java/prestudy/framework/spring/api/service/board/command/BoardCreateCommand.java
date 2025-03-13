package prestudy.framework.spring.api.service.board.command;

import lombok.Builder;
import lombok.Getter;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.user.User;

@Getter
public class BoardCreateCommand {

    private final String title;
    private final String content;

    @Builder
    private BoardCreateCommand(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Board toEntity(User user) {
        return Board.builder()
            .title(title)
            .content(content)
            .user(user)
            .build();
    }
}
