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

    @NotBlank(message = "작성자는 필수 값 입니다.")
    private String writer;

    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;

    @Builder
    public BoardCreateRequest(String title, String content, String writer, String password) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
    }

    public BoardCreateCommand toCommand() {
        return BoardCreateCommand.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .password(password)
            .build();
    }
}
