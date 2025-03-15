package prestudy.framework.spring.api.service.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.api.controller.comment.response.CommentResponse;
import prestudy.framework.spring.api.service.board.command.BoardCreateCommand;
import prestudy.framework.spring.api.service.board.command.BoardDeleteCommand;
import prestudy.framework.spring.api.service.board.command.BoardUpdateCommand;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;
import prestudy.framework.spring.domain.comment.Comment;
import prestudy.framework.spring.domain.comment.CommentRepository;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;

class BoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("게시글 조회")
    @Test
    void getBoards() {
        // given
        User user = User.ofUser("abcd1", "Password12!");
        userRepository.save(user);

        Board board1 = createBoardEntity("제목", "내용", user);
        Board board2 = createBoardEntity("다음글 제목", "다음글 내용", user);

        boardRepository.saveAll(List.of(board1, board2));

        Comment comment1 = Comment.builder()
            .content("댓글 1")
            .user(user)
            .board(board1)
            .build();

        Comment comment2 = Comment.builder()
            .content("댓글 2")
            .user(user)
            .board(board2)
            .build();

        Comment comment3 = Comment.builder()
            .content("댓글 3")
            .user(user)
            .board(board2)
            .build();

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        // when
        List<BoardResponse> boards = boardService.getBoards();

        // then
        assertThat(boards).hasSize(2)
            .extracting("title", "content", "writer")
            .containsExactly(
                tuple("다음글 제목", "다음글 내용", "abcd1"),
                tuple("제목", "내용", "abcd1")
            );

        assertThat(boards)
            .extracting(board -> board.getComments().stream()
                .map(CommentResponse::getContent)
                .toList())
            .containsExactly(
                List.of("댓글 3", "댓글 2"),
                List.of("댓글 1")
            );
    }

    @DisplayName("게시글 작성 시 토큰이 유효해야한다.")
    @Test
    void createBoardWithInvalidToken() {
        // given
        given(authenticationUserProvider.authenticatedUser()).willThrow(new IllegalStateException("토큰이 유효하지 않습니다."));

        BoardCreateCommand command = BoardCreateCommand.builder()
            .title("제목")
            .content("내용")
            .build();

        // when & then
        assertThatThrownBy(() -> boardService.createBoard(command))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("게시글을 작성한다.")
    @Test
    void createBoard() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        userRepository.save(user);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        BoardCreateCommand createCommand = BoardCreateCommand.builder()
            .title("제목")
            .content("내용")
            .build();

        // when
        BoardResponse response = boardService.createBoard(createCommand);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
        assertThat(response.getWriter()).isEqualTo("123abc");
    }

    @DisplayName("게시글 ID로 게시글을 상세 조회한다.")
    @Test
    void getBoardById() {
        // given
        User user = User.ofUser("abcd1", "Password12!");
        userRepository.save(user);

        Board board = createBoardEntity("제목", "내용", user);
        Board savedBoard = boardRepository.save(board);

        Comment comment1 = Comment.builder()
            .content("댓글 1")
            .user(user)
            .board(board)
            .build();

        Comment comment2 = Comment.builder()
            .content("댓글 2")
            .user(user)
            .board(board)
            .build();

        Comment comment3 = Comment.builder()
            .content("댓글 3")
            .user(user)
            .board(board)
            .build();

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        // when
        BoardResponse response = boardService.getBoardById(savedBoard.getId());

        // then
        assertThat(response.getId()).isEqualTo(savedBoard.getId());
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
        assertThat(response.getWriter()).isEqualTo("abcd1");
        assertThat(response.getComments())
            .extracting("content")
            .containsExactly(
                "댓글 3", "댓글 2", "댓글 1"
            );
    }

    @DisplayName("게시글 상세 조회시 ID는 유효해야 한다.")
    @Test
    void getBoardByInvalidId() {
        // given
        User user = User.ofUser("abcd1", "Password12!");
        userRepository.save(user);

        Board board = createBoardEntity("제목", "내용", user);
        Board savedBoard = boardRepository.save(board);

        // when & then
        long invalidId = savedBoard.getId() + 1;

        assertThatThrownBy(() -> boardService.getBoardById(invalidId))
            .hasMessage("게시글이 존재하지 않습니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게시글 수정 시 토큰이 유효해야한다.")
    @Test
    void updateBoardWithInvalidToken() {
        // given
        given(authenticationUserProvider.authenticatedUser()).willThrow(new IllegalStateException("토큰이 유효하지 않습니다."));

        BoardUpdateCommand command = BoardUpdateCommand.builder()
            .id(1L)
            .title("제목 수정")
            .content("내용 수정")
            .build();

        // when & then
        assertThatThrownBy(() -> boardService.updateBoard(command))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("게시글 수정 시 게시글이 존재해야한다.")
    @Test
    void updateBoardWithInvalidId() {
        // given
        User user = User.ofUser("abcd1", "Password12!");
        userRepository.save(user);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        Board board = createBoardEntity("제목", "내용", user);
        Board savedBoard = boardRepository.save(board);

        long invalidId = savedBoard.getId() + 1;

        BoardUpdateCommand command = BoardUpdateCommand.builder()
            .id(invalidId)
            .title("제목")
            .content("내용")
            .build();

        // when & then
        assertThatThrownBy(() -> boardService.updateBoard(command))
            .hasMessage("게시글이 존재하지 않습니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게시글 수정 시 직접 작성한 게시글이여야 한다.")
    @Test
    void updateBoardWithOwnBoard() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        User tokenUser = User.ofUser("123abcd", "Password12!");
        userRepository.save(user);
        userRepository.save(tokenUser);

        given(authenticationUserProvider.authenticatedUser()).willReturn(tokenUser);

        Board board = createBoardEntity("제목", "내용", user);
        boardRepository.save(board);

        BoardUpdateCommand command = BoardUpdateCommand.builder()
            .id(board.getId())
            .title("제목")
            .content("내용")
            .build();

        // when & then
        assertThatThrownBy(() -> boardService.updateBoard(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("작성자만 삭제/수정할 수 있습니다.");
    }

    @DisplayName("게시글 수정 시 관리자 권한을 가진 사용자라면 수정이 가능하다.")
    @Test
    void updateBoardWithAdmin() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        User admin = User.ofAdmin("123abcd", "Password12!");
        userRepository.save(user);
        userRepository.save(admin);

        given(authenticationUserProvider.authenticatedUser()).willReturn(admin);

        Board board = createBoardEntity("제목", "내용", user);
        boardRepository.save(board);

        BoardUpdateCommand command = BoardUpdateCommand.builder()
            .id(board.getId())
            .title("제목 수정")
            .content("내용 수정")
            .build();

        // when
        BoardResponse response = boardService.updateBoard(command);

        // then
        assertThat(response.getTitle()).isEqualTo("제목 수정");
        assertThat(response.getContent()).isEqualTo("내용 수정");
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void updateBoard() {
        // given
        User user = User.ofUser("abcd1", "Password12!");
        userRepository.save(user);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        Board board = createBoardEntity("제목", "내용", user);
        Board savedBoard = boardRepository.save(board);

        // when
        BoardUpdateCommand command = BoardUpdateCommand.builder()
            .id(savedBoard.getId())
            .title("제목 수정")
            .content("내용 수정")
            .build();

        BoardResponse response = boardService.updateBoard(command);

        //then
        assertThat(response.getTitle()).isEqualTo("제목 수정");
        assertThat(response.getContent()).isEqualTo("내용 수정");
        assertThat(response.getWriter()).isEqualTo("abcd1");
    }

    @DisplayName("게시글 삭제 시 토큰이 유효해야 한다.")
    @Test
    void deleteBoardWithInvalidToken() {
        // given
        given(authenticationUserProvider.authenticatedUser()).willThrow(new IllegalStateException("토큰이 유효하지 않습니다."));

        BoardDeleteCommand command = BoardDeleteCommand.of(1L);

        // when & then
        assertThatThrownBy(() -> boardService.deleteBoard(command))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("게시글 삭제 시 게시글은 존재해야한다.")
    @Test
    void deleteBoardWithInvalidId() {
        // given
        User user = User.ofUser("abcd1", "Password12!");
        userRepository.save(user);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        Board board = createBoardEntity("제목", "내용", user);
        Board savedBoard = boardRepository.save(board);

        long invalidId = savedBoard.getId() + 1;
        BoardDeleteCommand command = BoardDeleteCommand.of(invalidId);

        // when & then
        assertThatThrownBy(() -> boardService.deleteBoard(command))
            .hasMessage("게시글이 존재하지 않습니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게시글 삭제 시 직접 작성한 게시글이여야 한다.")
    @Test
    void deleteBoardWithOwnBoard() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        User tokenUser = User.ofUser("123abcd", "Password12!");
        userRepository.save(user);
        userRepository.save(tokenUser);

        given(authenticationUserProvider.authenticatedUser()).willReturn(tokenUser);

        Board board = createBoardEntity("제목", "내용", user);
        boardRepository.save(board);

        BoardDeleteCommand command = BoardDeleteCommand.of(board.getId());

        // when & then
        assertThatThrownBy(() -> boardService.deleteBoard(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("작성자만 삭제/수정할 수 있습니다.");
    }

    @DisplayName("게시물을 삭제 시 관리자 권한을 가진 사용자라면 삭제가 가능하다.")
    @Test
    void deleteBoardWithAdmin() {
        // given
        User user = User.ofUser("abcd1", "Password12!");
        User admin = User.ofAdmin("abcd2", "Password12!");
        userRepository.save(user);
        userRepository.save(admin);

        given(authenticationUserProvider.authenticatedUser()).willReturn(admin);

        Board board = createBoardEntity("제목", "내용", user);
        boardRepository.save(board);

        BoardDeleteCommand command = BoardDeleteCommand.of(board.getId());

        // when
        boardService.deleteBoard(command);

        // then
        Optional<Board> findBoard = boardRepository.findById(board.getId());
        assertThat(findBoard).isNotPresent();
    }

    @DisplayName("게시물을 삭제한다.")
    @Test
    void deleteBoard() {
        // given
        User user = User.ofUser("abcd1", "Password12!");
        userRepository.save(user);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        Board board = createBoardEntity("제목", "내용", user);
        boardRepository.save(board);

        BoardDeleteCommand command = BoardDeleteCommand.of(board.getId());

        // when
        boardService.deleteBoard(command);

        // then
        Optional<Board> findBoard = boardRepository.findById(board.getId());
        assertThat(findBoard).isNotPresent();
    }

    private Board createBoardEntity(String title, String content, User user) {
        return Board.builder()
            .title(title)
            .content(content)
            .user(user)
            .build();
    }
}