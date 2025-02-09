package prestudy.framework.spring.api.service.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.api.service.board.command.BoardCreateCommand;
import prestudy.framework.spring.api.service.board.command.BoardUpdateCommand;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.board.BoardRepository;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
            .extracting("title", "content", "writer", "createdDate")
            .containsExactly(
                tuple("다음글 제목", "다음글 내용", "홍길동", LocalDateTime.of(2025, 2, 7, 12, 1)),
                tuple("제목", "내용", "홍길동", LocalDateTime.of(2025, 2, 7, 12, 0))
            );
    }

    @DisplayName("게시글을 작성한다.")
    @Test
    void createBoard() {
        // given
        BoardCreateCommand createCommand = BoardCreateCommand.builder()
            .title("제목")
            .content("내용")
            .writer("홍길동")
            .password("1234")
            .build();

        // when
        BoardResponse response = boardService.createBoard(createCommand);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
        assertThat(response.getWriter()).isEqualTo("홍길동");
    }

    @DisplayName("게시글 ID로 게시글을 상세 조회한다.")
    @Test
    void getBoardById() {
        // given
        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .writer("홍길동")
            .password("<PASSWORD>")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        Board savedBoard = boardRepository.save(board);

        // when
        BoardResponse response = boardService.getBoardById(savedBoard.getId());

        // then
        assertThat(response.getId()).isEqualTo(savedBoard.getId());
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
        assertThat(response.getWriter()).isEqualTo("홍길동");
        assertThat(response.getCreatedDate()).isEqualTo(LocalDateTime.of(2025, 2, 7, 12, 0));
    }

    @DisplayName("게시글 상세 조회시 ID는 유효해야 한다.")
    @Test
    void getBoardByInvalidId() {
        // given
        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .writer("홍길동")
            .password("<PASSWORD>")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        Board savedBoard = boardRepository.save(board);

        // when & then
        long invalidId = savedBoard.getId() + 1;

        assertThatThrownBy(() -> boardService.getBoardById(invalidId))
            .hasMessage("존재하지 않는 게시글입니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("수정 시 ID는 유효해야 한다.")
    @Test
    void updateBoardWithInvalidId() {
        // given
        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .writer("홍길동")
            .password("<PASSWORD>")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        Board savedBoard = boardRepository.save(board);

        // when & then
        long invalidId = savedBoard.getId() + 1;

        BoardUpdateCommand command = BoardUpdateCommand.builder()
            .id(invalidId)
            .title("제목")
            .content("내용")
            .password("<PASSWORD>")
            .build();

        assertThatThrownBy(() -> boardService.updateBoard(command))
            .hasMessage("존재하지 않는 게시글입니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("수정 시 패스워드는 일치해야한다.")
    @Test
    void updateBoardWithInvalidPassword() {
        // given
        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .writer("홍길동")
            .password("<PASSWORD>")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        Board savedBoard = boardRepository.save(board);

        // when & then
        BoardUpdateCommand command = BoardUpdateCommand.builder()
            .id(savedBoard.getId())
            .title("제목")
            .content("내용")
            .password("<INVALID PASSWORD>")
            .build();

        assertThatThrownBy(() -> boardService.updateBoard(command))
            .hasMessage("비밀번호가 일치하지 않습니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void updateBoard() {
        // given
        Board board = Board.builder()
            .title("제목")
            .content("내용")
            .writer("홍길동")
            .password("<PASSWORD>")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        Board savedBoard = boardRepository.save(board);

        // when
        BoardUpdateCommand command = BoardUpdateCommand.builder()
            .id(savedBoard.getId())
            .title("제목 수정")
            .content("내용 수정")
            .writer("홍길순")
            .password("<PASSWORD>")
            .build();

        BoardResponse response = boardService.updateBoard(command);

        //then
        assertThat(response.getTitle()).isEqualTo("제목 수정");
        assertThat(response.getContent()).isEqualTo("내용 수정");
        assertThat(response.getWriter()).isEqualTo("홍길순");
    }
}