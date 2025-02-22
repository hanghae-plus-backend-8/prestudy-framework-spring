package prestudy.framework.spring.api.controller.board.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.service.board.command.BoardDeleteCommand;

@Getter
@NoArgsConstructor
public class BoardDeleteRequest {

    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;

    @Builder
    private BoardDeleteRequest(String password) {
        this.password = password;
    }

    public BoardDeleteCommand toCommand(Long id) {
        return BoardDeleteCommand.builder()
            .id(id)
            .password(password)
            .build();
    }
}
