package prestudy.framework.spring.api.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.api.controller.comment.response.CommentResponse;
import prestudy.framework.spring.api.authenticate.AuthenticationUserProvider;
import prestudy.framework.spring.api.service.board.command.BoardCreateCommand;
import prestudy.framework.spring.api.service.board.command.BoardDeleteCommand;
import prestudy.framework.spring.api.service.board.command.BoardUpdateCommand;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;
import prestudy.framework.spring.domain.comment.CommentRepository;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final AuthenticationUserProvider authenticationUserProvider;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoards() {
        List<Board> boards = boardRepository.findByOrderByCreatedDateTimeDesc();
        return boards.stream()
            .map(this::responseWithComments)
            .toList();
    }

    public BoardResponse createBoard(BoardCreateCommand createCommand) {
        User user = getCurrentUser();
        Board savedBoard = boardRepository.save(createCommand.toEntity(user));

        return BoardResponse.of(savedBoard);
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoardById(Long id) {
        Board findBoard = findBoardById(id);
        return responseWithComments(findBoard);
    }

    public BoardResponse updateBoard(BoardUpdateCommand command) {
        User user = getCurrentUser();
        Board board = findBoardById(command.getId());

        if (hasNotPermission(board, user)) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        board.updateTitle(command.getTitle());
        board.updateContent(command.getContent());

        return BoardResponse.of(board);
    }

    private BoardResponse responseWithComments(Board board) {
        List<CommentResponse> comments = commentRepository.findByBoardIdOrderByCreatedDateTimeDesc(board.getId())
            .stream()
            .map(CommentResponse::of)
            .toList();
        return BoardResponse.of(board, comments);
    }

    public void deleteBoard(BoardDeleteCommand command) {
        User user = getCurrentUser();
        Board board = findBoardById(command.getId());

        if (hasNotPermission(board, user)) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        boardRepository.delete(board);
    }

    private Board findBoardById(Long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private User getCurrentUser() {
        Long userId = authenticationUserProvider.getUserId();
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("토큰이 유효하지 않습니다."));
    }

    private boolean hasNotPermission(Board board, User user) {
        return board.isNotWriter(user) && user.isNotAdmin();
    }
}
