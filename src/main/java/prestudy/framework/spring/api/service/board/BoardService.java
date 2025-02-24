package prestudy.framework.spring.api.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.api.service.board.command.BoardCreateCommand;
import prestudy.framework.spring.api.service.board.command.BoardDeleteCommand;
import prestudy.framework.spring.api.service.board.command.BoardUpdateCommand;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoards() {
        List<Board> boards = boardRepository.findByOrderByCreatedDateTimeDesc();
        return boards.stream()
            .map(BoardResponse::of)
            .toList();
    }

    public BoardResponse createBoard(BoardCreateCommand createCommand) {
        Board savedBoard = boardRepository.save(createCommand.toEntity());
        return BoardResponse.of(savedBoard);
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoardById(Long id) {
        Board findBoard = findBoardById(id);
        return BoardResponse.of(findBoard);
    }

    public BoardResponse updateBoard(BoardUpdateCommand command) {
        Board findBoard = findBoardById(command.getId());
        if (findBoard.isInvalidPassword(command.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        findBoard.updateTitle(command.getTitle());
        findBoard.updateContent(command.getContent());
        findBoard.updateWriter(command.getWriter());

        return BoardResponse.of(findBoard);
    }

    public void deleteBoard(BoardDeleteCommand command) {
        Board findBoard = findBoardById(command.getId());
        if (findBoard.isInvalidPassword(command.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        boardRepository.delete(findBoard);
    }

    private Board findBoardById(Long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }
}
