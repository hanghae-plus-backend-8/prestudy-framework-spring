package prestudy.framework.spring.api.service.comment.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreateCommand {

    private final Long boardId;
    private final String content;

    @Builder
    private CommentCreateCommand(Long boardId, String content) {
        this.boardId = boardId;
        this.content = content;
    }
}
