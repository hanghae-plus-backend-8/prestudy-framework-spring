package hanghaeboard.domain.board;

import hanghaeboard.api.service.board.response.FindBoardResponse;
import hanghaeboard.config.AuditingConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@ActiveProfiles("test")
@Import(AuditingConfig.class)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
    }

    @DisplayName("게시물을 생성할 수 있다.")
    @Test
    void createBoard() {
        // given
        Board board = makeBoard("yeop", "1234", "hi", "hihihi");

        // when
        boardRepository.save(board);

        // then
        List<Board> all = boardRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all)
                .extracting("writer", "password", "title", "content")
                .containsExactlyInAnyOrder(tuple("yeop", "1234","hi", "hihihi"));
    }

    @DisplayName("생성 일자로 내림차순 정렬된 게시물 목록을 조회할 수 있다.")
    @Test
    void findAllBoard() throws Exception {
        // given
        Board board1 = makeBoard("yeop", "1234", "title1", "content1");
        Thread.sleep(10);
        Board board2 = makeBoard("yeop", "1234", "title2", "content2");
        Thread.sleep(10);
        Board board3 = makeBoard("yeop", "1234", "title3", "content3");

        boardRepository.saveAll(List.of(board1, board2, board3));

        // when
        List<FindBoardResponse> allBoard = boardRepository.findAllBoard();

        // then
        assertThat(allBoard).extracting("id", "writer", "title", "content")
                .containsExactly(
                        tuple(3L, "yeop", "title3", "content3")
                        , tuple(2L, "yeop", "title2", "content2")
                        , tuple(1L, "yeop", "title1", "content1")
                );
    }

    @DisplayName("게시물을 id로 조회할 수 있다.")
    @Test
    void findBoardById() {
        // given
        Board board = makeBoard("yeop", "1234", "title1", "content1");
        Board save = boardRepository.save(board);
        Long id = save.getId();

        // when
        Board findById = boardRepository.findById(id).get();

        // then
        assertThat(findById.getId()).isEqualTo(id);
        assertThat(findById.getTitle()).isEqualTo("title1");
    }

    private static Board makeBoard(String writer, String password, String title, String content) {
        return Board.builder().writer(writer).password(password).title(title).content(content).build();
    }
}