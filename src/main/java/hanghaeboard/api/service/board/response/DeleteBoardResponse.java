package hanghaeboard.api.service.board.response;

import hanghaeboard.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DeleteBoardResponse {

    private LocalDateTime deletedDatetime;

    @Builder
    private DeleteBoardResponse(LocalDateTime deletedDatetime) {
        this.deletedDatetime = deletedDatetime;
    }

    public static DeleteBoardResponse from(Board board){
        return DeleteBoardResponse.builder().deletedDatetime(board.getDeletedDatetime()).build();
    }
}
