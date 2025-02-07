package prestudy.framework.spring.api.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.api.service.board.command.BoardCreateCommand;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<BoardResponse> getBoards() {
        List<Board> boards = boardRepository.findByOrderByCreatedDateDesc();
        return boards.stream()
            .map(BoardResponse::of)
            .toList();
    }

    public BoardResponse createBoard(BoardCreateCommand createCommand) {
        Board savedBoard = boardRepository.save(createCommand.toEntity());
        return BoardResponse.of(savedBoard);
    }
}
