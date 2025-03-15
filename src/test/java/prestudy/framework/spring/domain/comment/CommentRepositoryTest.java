package prestudy.framework.spring.domain.comment;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class CommentRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("게시글 ID로 댓글을 내림차순 조회한다.")
    @Test
    void findByBoardIdOrderByCreatedDateTimeDesc() {
        // given
        User user1 = User.ofUser("abcd1", "Password12!");
        User user2 = User.ofUser("abcd2", "Password12!");
        userRepository.save(user1);
        userRepository.save(user2);

        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .user(user1)
            .build();
        boardRepository.save(board);

        Comment comment1 = Comment.builder()
            .content("댓글 1")
            .user(user1)
            .board(board)
            .build();

        Comment comment2 = Comment.builder()
            .content("댓글 2")
            .user(user2)
            .board(board)
            .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        // when
        List<Comment> results = commentRepository.findByBoardIdOrderByCreatedDateTimeDesc(board.getId());

        // then
        assertThat(results).hasSize(2)
            .extracting("content", "user")
            .containsExactly(
                tuple("댓글 2", user2),
                tuple("댓글 1", user1)
            );
    }

}