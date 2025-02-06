package hanghaeboard.api.service.board;

import hanghaeboard.api.service.board.request.CreateBoardServiceRequest;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public CreateBoardResponse createBoard(CreateBoardServiceRequest request) {

        Board board = request.toEntity();
        Board savedBoard = boardRepository.save(board);

        return CreateBoardResponse.from(savedBoard);
    }

}
