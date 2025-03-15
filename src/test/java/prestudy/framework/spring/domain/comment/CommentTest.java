package prestudy.framework.spring.domain.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

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

}