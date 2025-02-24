package hanghaeboard.api.controller.board.request;

import hanghaeboard.domain.board.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBoardRequest {


    @NotBlank(message = "작성자명은 필수 입력입니다.")
    private String writer;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    private String password;

    @NotBlank(message = "제목은 필수 입력입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력입니다.")
    private String content;

    @Builder
    private CreateBoardRequest(String writer, String password, String title, String content) {
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
    }

    public Board toEntity() {
        return Board.builder()
                .writer(writer)
                .password(password)
                .title(title)
                .content(content)
                .build();
    }
}
