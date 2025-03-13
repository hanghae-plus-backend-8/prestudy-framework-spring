package prestudy.framework.spring.api.controller.board.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.service.board.command.BoardCreateCommand;

@Getter
@NoArgsConstructor
public class BoardCreateRequest {

    @NotBlank(message = "제목은 필수 값 입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 값 입니다.")
    private String content;

    @Builder
    private BoardCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public BoardCreateCommand toCommand() {
        return BoardCreateCommand.builder()
            .title(title)
            .content(content)
            .build();
    }
}
