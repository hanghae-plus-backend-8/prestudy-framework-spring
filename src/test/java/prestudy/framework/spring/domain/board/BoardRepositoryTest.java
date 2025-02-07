package prestudy.framework.spring.domain.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@Transactional
class BoardRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private BoardRepository boardRepository;

    @DisplayName("전체 게시글 목록 조회")
    @Test
    void findByOrderByCreatedDateDesc() {
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
        List<Board> boards = boardRepository.findByOrderByCreatedDateDesc();

        // then
        assertThat(boards)
            .hasSize(2)
            .extracting("title", "content")
            .containsExactly(
                tuple("다음글 제목", "다음글 내용"),
                tuple("제목", "내용")
            );
    }
}