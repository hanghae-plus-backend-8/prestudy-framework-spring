package prestudy.framework.spring.domain.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @DisplayName("패스워드가 일치하지 않으면 참이다.")
    @Test
    void isInvalidPassword() {
        // given
        Board board = createBoard();

        // when
        boolean result = board.isInvalidPassword("<INVALID PASSWORD>");

        // then
        assertThat(result).isTrue();
    }

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

    @DisplayName("게시글의 작성자명이 공백이면 변경하지 않는다.")
    @MethodSource("nullOrBlankRequest")
    @ParameterizedTest
    void updateWriterWithBlank(String updateWriter) {
        // given
        Board board = createBoard();

        // when
        board.updateWriter(updateWriter);

        // then
        assertThat(board.getWriter()).isEqualTo("홍길동");
    }

    @DisplayName("게시글의 작성자명을 변경한다.")
    @Test
    void updateWriter() {
        // given
        Board board = createBoard();

        // when
        board.updateWriter("홍길순");

        // then
        assertThat(board.getWriter()).isEqualTo("홍길순");
    }

    private static Stream<String> nullOrBlankRequest() {
        return Stream.of(
            null,
            "   ",
            ""
        );
    }

    private Board createBoard() {
        return Board.builder()
            .title("제목")
            .content("내용")
            .writer("홍길동")
            .password("<PASSWORD>")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();
    }
}