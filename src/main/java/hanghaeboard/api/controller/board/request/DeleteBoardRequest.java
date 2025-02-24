package hanghaeboard.api.controller.board.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteBoardRequest {

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    private String password;

    @Builder
    private DeleteBoardRequest(String password) {
        this.password = password;
    }
}
