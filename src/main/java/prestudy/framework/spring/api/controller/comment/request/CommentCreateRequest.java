package prestudy.framework.spring.api.controller.comment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.service.comment.command.CommentCreateCommand;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "내용은 필수 값 입니다.")
    private String content;

    private CommentCreateRequest(String content) {
        this.content = content;
    }

    public static CommentCreateRequest of(String content) {
        return new CommentCreateRequest(content);
    }

    public CommentCreateCommand toCommand(Long boardId) {
        return CommentCreateCommand.builder()
            .boardId(boardId)
            .content(content)
            .build();
    }
}
