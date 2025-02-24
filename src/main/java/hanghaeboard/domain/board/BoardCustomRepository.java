package hanghaeboard.domain.board;

import hanghaeboard.api.service.board.response.FindBoardResponse;

import java.util.List;

public interface BoardCustomRepository {
    List<FindBoardResponse> findAllBoard();
}
