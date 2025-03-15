package prestudy.framework.spring.api.service.comment.command;

import lombok.Getter;

@Getter
public class CommentDeleteCommand {

    private final Long id;

    private CommentDeleteCommand(Long id) {
        this.id = id;
    }

    public static CommentDeleteCommand of(Long id) {
        return new CommentDeleteCommand(id);
    }
}
