package prestudy.framework.spring.domain.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import prestudy.framework.spring.domain.user.User;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @DisplayName("게시글의 제목이 공백이면 변경하지 않는다.")
    @MethodSource("nullOrBlankRequest")
    @ParameterizedTest
    void updateTitleWithBlank(String updateTitle) {
        // given
        Board board = createBoard();

        // when
        board.updateTitle(updateTitle);

        // then
        assertThat(board.getTitle()).isEqualTo("제목");
    }

    @DisplayName("게시글의 제목을 변경한다.")
    @Test
    void updateTitle() {
        // given
        Board board = createBoard();

        // when
        board.updateTitle("제목 변경");

        // then
        assertThat(board.getTitle()).isEqualTo("제목 변경");
    }

    @DisplayName("게시글의 내용이 공백이면 변경하지 않는다.")
    @MethodSource("nullOrBlankRequest")
    @ParameterizedTest
    void updateContentWithBlank(String updateContent) {
        // given
        Board board = createBoard();

        // when
        board.updateContent(updateContent);

        // then
        assertThat(board.getContent()).isEqualTo("내용");
    }

    @DisplayName("게시글의 내용을 변경한다.")
    @Test
    void updateContent() {
        // given
        Board board = createBoard();

        // when
        board.updateContent("내용 변경");

        // then
        assertThat(board.getContent()).isEqualTo("내용 변경");
    }

    private static Stream<String> nullOrBlankRequest() {
        return Stream.of(
            null,
            "   ",
            ""
        );
    }

    private Board createBoard() {
        User user = User.ofUser("123abc", "Password12!");

        return Board.builder()
            .title("제목")
            .content("내용")
            .user(user)
            .build();
    }
}