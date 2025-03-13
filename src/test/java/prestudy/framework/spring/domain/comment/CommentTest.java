package prestudy.framework.spring.domain.comment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;
import prestudy.framework.spring.support.IntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("댓글 내용을 수정한다.")
    @ParameterizedTest
    @CsvSource({
        "댓글 변경, 댓글 변경",
        ", 댓글 내용",
        "'', 댓글 내용"
    })
    void updateContent(String updatedContent, String expectedContent) {
        // given
        User user = User.ofUser("123abc", "Password12!");
        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .user(user)
            .build();

        Comment comment = Comment.builder()
            .content("댓글 내용")
            .user(user)
            .board(board)
            .build();

        // when
        comment.updateContent(updatedContent);

        // then
        assertThat(comment.getContent()).isEqualTo(expectedContent);
    }

    @DisplayName("사용자는 댓글 작성자이다.")
    @Test
    void isWriter() {
        // given
        User user = User.ofUser("123abc", "Password12!");

        userRepository.save(user);

        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .user(user)
            .build();

        Comment comment = Comment.builder()
            .content("내용")
            .user(user)
            .board(board)
            .build();

        // when
        boolean result = comment.isNotWriter(user);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("사용자는 댓글 작성자가 아니다.")
    @Test
    void isNotWriter() {
        // given
        User commentUser = User.ofUser("123abc", "Password12!");
        User tokenUser = User.ofUser("123abcd", "Password12!");

        userRepository.save(commentUser);
        userRepository.save(tokenUser);

        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .user(commentUser)
            .build();

        Comment comment = Comment.builder()
            .content("내용")
            .user(commentUser)
            .board(board)
            .build();

        // when
        boolean result = comment.isNotWriter(tokenUser);

        // then
        assertThat(result).isTrue();
    }

}