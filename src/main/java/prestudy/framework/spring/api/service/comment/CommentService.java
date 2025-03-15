package prestudy.framework.spring.api.service.comment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prestudy.framework.spring.api.controller.comment.response.CommentResponse;
import prestudy.framework.spring.api.authenticate.AuthenticationUserProvider;
import prestudy.framework.spring.api.service.comment.command.CommentCreateCommand;
import prestudy.framework.spring.api.service.comment.command.CommentDeleteCommand;
import prestudy.framework.spring.api.service.comment.command.CommentUpdateCommand;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;
import prestudy.framework.spring.domain.comment.Comment;
import prestudy.framework.spring.domain.comment.CommentRepository;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final AuthenticationUserProvider authenticationUserProvider;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public CommentResponse createComment(CommentCreateCommand command) {
        User user = getCurrentUser();

        Board board = boardRepository.findById(command.getBoardId())
            .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
            .content(command.getContent())
            .user(user)
            .board(board)
            .build();

        commentRepository.save(comment);

        return CommentResponse.of(comment);
    }

    public CommentResponse updateComment(CommentUpdateCommand command) {
        User user = getCurrentUser();
        Comment comment = findCommentBy(command.getId());

        if (hasNotPermission(comment, user)) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        comment.updateContent(command.getContent());

        return CommentResponse.of(comment);
    }

    public void deleteComment(CommentDeleteCommand command) {
        User user = getCurrentUser();
        Comment comment = findCommentBy(command.getId());

        if (hasNotPermission(comment, user)) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    private User getCurrentUser() {
        Long userId = authenticationUserProvider.getUserId();
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("토큰이 유효하지 않습니다."));
    }

    private Comment findCommentBy(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
    }

    private boolean hasNotPermission(Comment comment, User user) {
        return comment.isNotWriter(user) && user.isNotAdmin();
    }
}
