package prestudy.framework.spring.api.controller.comment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.service.comment.command.CommentUpdateCommand;

@Getter
@NoArgsConstructor
public class CommentUpdateRequest {

    @NotBlank(message = "내용은 필수 값 입니다.")
    private String content;

    private CommentUpdateRequest(String content) {
        this.content = content;
    }

    public static CommentUpdateRequest of(String content) {
        return new CommentUpdateRequest(content);
    }

    public CommentUpdateCommand toCommand(Long id) {
        return CommentUpdateCommand.builder()
            .id(id)
            .content(content)
            .build();
    }
}
