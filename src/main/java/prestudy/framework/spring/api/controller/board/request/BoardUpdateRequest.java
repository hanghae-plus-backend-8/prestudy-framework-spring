package prestudy.framework.spring.api.controller.board.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.service.board.command.BoardUpdateCommand;

@Getter
@NoArgsConstructor
public class BoardUpdateRequest {

    private String title;

    private String content;

    @Builder
    private BoardUpdateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public BoardUpdateCommand toCommand(Long id) {
        return BoardUpdateCommand.builder()
            .id(id)
            .title(title)
            .content(content)
            .build();
    }
}
