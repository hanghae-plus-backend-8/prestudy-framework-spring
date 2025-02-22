package prestudy.framework.spring.api.controller.board.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.service.board.command.BoardUpdateCommand;

@Getter
@NoArgsConstructor
public class BoardUpdateRequest {

    private String title;

    private String content;

    private String writer;

    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;

    @Builder
    private BoardUpdateRequest(String title, String content, String writer, String password) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
    }

    public BoardUpdateCommand toCommand(Long id) {
        return BoardUpdateCommand.builder()
            .id(id)
            .title(title)
            .content(content)
            .writer(writer)
            .password(password)
            .build();
    }
}
