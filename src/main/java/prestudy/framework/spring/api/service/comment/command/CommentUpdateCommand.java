package prestudy.framework.spring.api.service.comment.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUpdateCommand {

    private final Long id;
    private final String content;

    @Builder
    private CommentUpdateCommand(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
