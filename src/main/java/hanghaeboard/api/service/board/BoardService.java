package hanghaeboard.api.service.board;

import hanghaeboard.api.service.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public CreateBoardResponse createBoard(CreateBoardRequest request) {
        Board board = request.toEntity();
        Board savedBoard = boardRepository.save(board);

        return CreateBoardResponse.from(savedBoard);
    }

}
