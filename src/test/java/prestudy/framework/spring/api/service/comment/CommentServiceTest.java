package prestudy.framework.spring.api.service.comment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.api.controller.comment.response.CommentResponse;
import prestudy.framework.spring.api.service.comment.command.CommentCreateCommand;
import prestudy.framework.spring.api.service.comment.command.CommentDeleteCommand;
import prestudy.framework.spring.api.service.comment.command.CommentUpdateCommand;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;
import prestudy.framework.spring.domain.comment.Comment;
import prestudy.framework.spring.domain.comment.CommentRepository;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;
import prestudy.framework.spring.support.IntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class CommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("댓글 작성 시 토큰이 유효해야한다.")
    @Test
    void createCommentWithInvalidToken() {
        // given
        given(authenticationUserProvider.authenticatedUser()).willThrow(new IllegalStateException("토큰이 유효하지 않습니다."));

        CommentCreateCommand command = CommentCreateCommand.builder()
            .boardId(1L)
            .content("댓글 내용")
            .build();

        // when & then
        assertThatThrownBy(() -> commentService.createComment(command))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("댓글 작성 시 게시글은 유효해야한다.")
    @Test
    void createCommentWithInvalidBoard() {
        // given
        User user = User.ofUser("123abc", "Password12!");

        userRepository.save(user);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        CommentCreateCommand command = CommentCreateCommand.builder()
            .boardId(1L)
            .content("댓글 내용")
            .build();

        // when & then
        assertThatThrownBy(() -> commentService.createComment(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("게시글이 존재하지 않습니다.");
    }

    @DisplayName("댓글을 작성한다.")
    @Test
    void createComment() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        userRepository.save(user);

        Board board = createBoardEntity(user);
        boardRepository.save(board);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        CommentCreateCommand command = CommentCreateCommand.builder()
            .boardId(board.getId())
            .content("댓글 내용")
            .build();

        // when
        CommentResponse response = commentService.createComment(command);

        // then
        assertThat(response.getContent()).isEqualTo("댓글 내용");
    }

    @DisplayName("댓글 수정 시 토큰이 유효해야 한다.")
    @Test
    void updateCommentWithInvalidToken() {
        // given
        given(authenticationUserProvider.authenticatedUser()).willThrow(new IllegalStateException("토큰이 유효하지 않습니다."));

        CommentUpdateCommand command = CommentUpdateCommand.builder()
            .id(1L)
            .content("댓글 내용")
            .build();

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(command))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("댓글 수정 시 작성한 댓글이 존재해야한다.")
    @Test
    void updateCommentWithoutComment() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        userRepository.save(user);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        CommentUpdateCommand command = CommentUpdateCommand.builder()
            .id(0L)
            .content("댓글 수정")
            .build();

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("댓글이 존재하지 않습니다.");
    }

    @DisplayName("댓글 수정 시 직접 작성한 댓글이여야 한다.")
    @Test
    void updateCommentWithOwnComment() {
        // given
        User commentUser = User.ofUser("123abc", "Password12!");
        User tokenUser = User.ofUser("123abcd", "Password12!");
        userRepository.save(commentUser);
        userRepository.save(tokenUser);

        Board board = createBoardEntity(commentUser);
        boardRepository.save(board);

        given(authenticationUserProvider.authenticatedUser()).willReturn(tokenUser);

        Comment comment = Comment.builder()
            .content("댓글 생성")
            .user(commentUser)
            .board(board)
            .build();

        commentRepository.save(comment);

        CommentUpdateCommand command = CommentUpdateCommand.builder()
            .id(comment.getId())
            .content("댓글 수정")
            .build();

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("작성자만 삭제/수정할 수 있습니다.");
    }

    @DisplayName("댓글 수정 시 관리자 권한을 가진 사용자라면 수정이 가능하다.")
    @Test
    void updateCommentWithAdmin() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        User admin = User.ofAdmin("123abcd", "Password12!");
        userRepository.save(user);
        userRepository.save(admin);

        Board board = createBoardEntity(user);
        boardRepository.save(board);

        given(authenticationUserProvider.authenticatedUser()).willReturn(admin);

        Comment comment = Comment.builder()
            .content("댓글 생성")
            .user(user)
            .board(board)
            .build();

        commentRepository.save(comment);

        CommentUpdateCommand command = CommentUpdateCommand.builder()
            .id(comment.getId())
            .content("댓글 수정")
            .build();

        // when
        CommentResponse response = commentService.updateComment(command);

        // then
        assertThat(response.getContent()).isEqualTo("댓글 수정");
    }

    @DisplayName("작성자가 댓글을 수정한다.")
    @Test
    void updateComment() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        userRepository.save(user);

        Board board = createBoardEntity(user);
        boardRepository.save(board);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        Comment comment = Comment.builder()
            .content("댓글 생성")
            .user(user)
            .board(board)
            .build();

        commentRepository.save(comment);

        CommentUpdateCommand command = CommentUpdateCommand.builder()
            .id(comment.getId())
            .content("댓글 수정")
            .build();

        // when
        CommentResponse response = commentService.updateComment(command);

        // then
        assertThat(response.getContent()).isEqualTo("댓글 수정");
    }

    @DisplayName("댓글 삭제 시 토큰이 유효해야 한다.")
    @Test
    void deleteCommentWithInvalidToken() {
        // given
        given(authenticationUserProvider.authenticatedUser()).willThrow(new IllegalStateException("토큰이 유효하지 않습니다."));

        CommentDeleteCommand command = CommentDeleteCommand.of(1L);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(command))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("댓글 삭제 시 작성한 댓글이 존재해야한다.")
    @Test
    void deleteCommentWithoutComment() {
        // given
        User user = User.ofUser("123abc", "Password12!");

        userRepository.save(user);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        CommentDeleteCommand command = CommentDeleteCommand.of(1L);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("댓글이 존재하지 않습니다.");
    }

    @DisplayName("댓글 삭제 시 직접 작성한 댓글이여야 한다.")
    @Test
    void deleteCommentWithOwnComment() {
        // given
        User commentUser = User.ofUser("123abc", "Password12!");
        User tokenUser = User.ofUser("123abcd", "Password12!");
        userRepository.save(commentUser);
        userRepository.save(tokenUser);

        Board board = createBoardEntity(commentUser);
        boardRepository.save(board);

        given(authenticationUserProvider.authenticatedUser()).willReturn(tokenUser);

        Comment comment = Comment.builder()
            .content("댓글 생성")
            .user(commentUser)
            .board(board)
            .build();

        commentRepository.save(comment);

        CommentDeleteCommand command = CommentDeleteCommand.of(comment.getId());

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("작성자만 삭제/수정할 수 있습니다.");
    }

    @DisplayName("댓글 삭제 시 관리자 권한을 가진 사용자라면 삭제가 가능하다.")
    @Test
    void deleteCommentWithAdmin() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        User admin = User.ofAdmin("123abcd", "Password12!");
        userRepository.save(user);
        userRepository.save(admin);

        Board board = createBoardEntity(user);

        boardRepository.save(board);

        given(authenticationUserProvider.authenticatedUser()).willReturn(admin);

        Comment comment = Comment.builder()
            .content("댓글 생성")
            .user(user)
            .board(board)
            .build();

        commentRepository.save(comment);

        CommentDeleteCommand command = CommentDeleteCommand.of(comment.getId());

        // when
        commentService.deleteComment(command);

        // then
        Comment deleteComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(deleteComment).isNull();
    }

    @DisplayName("작성자가 댓글을 삭제한다.")
    @Test
    void deleteComment() {
        // given
        User user = User.ofUser("123abc", "Password12!");
        userRepository.save(user);

        Board board = createBoardEntity(user);
        boardRepository.save(board);

        given(authenticationUserProvider.authenticatedUser()).willReturn(user);

        Comment comment = Comment.builder()
            .content("댓글 생성")
            .user(user)
            .board(board)
            .build();

        commentRepository.save(comment);

        CommentDeleteCommand command = CommentDeleteCommand.of(comment.getId());

        // when
        commentService.deleteComment(command);

        // then
        Comment deleteComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(deleteComment).isNull();
    }

    private Board createBoardEntity(User user) {
        return Board.builder()
            .title("제목")
            .content("내용")
            .user(user)
            .build();
    }

}