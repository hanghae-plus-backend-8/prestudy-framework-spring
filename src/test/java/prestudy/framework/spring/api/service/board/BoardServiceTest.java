package prestudy.framework.spring.api.service.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.support.IntegrationTestSupport;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class BoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
    }

    @DisplayName("게시글 조회")
    @Test
    void getBoards() {
        // given
        Board board1 = Board.builder()
            .title("제목")
            .content("내용")
            .writer("홍길동")
            .password("<PASSWORD>")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        Board board2 = Board.builder()
            .title("다음글 제목")
            .content("다음글 내용")
            .writer("홍길동")
            .password("<PASSWORD>")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 1))
            .build();

        boardRepository.saveAll(List.of(board1, board2));

        // when
        List<BoardResponse> boards = boardService.getBoards();

        // then
        assertThat(boards).hasSize(2)
            .extracting("title", "content")
            .containsExactly(
                tuple("다음글 제목", "다음글 내용"),
                tuple("제목", "내용")
            );
    }

}