package hanghaeboard.api.service.board;

import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.controller.board.request.DeleteBoardRequest;
import hanghaeboard.api.controller.board.request.UpdateBoardRequest;
import hanghaeboard.api.exception.exception.InvalidPasswordException;
import hanghaeboard.api.service.board.response.DeleteBoardResponse;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import hanghaeboard.api.service.board.response.UpdateBoardResponse;
import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.board.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

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

        CreateBoardRequest request = CreateBoardRequest.builder()
                .writer("yeop")
                .password("1234")
                .title("title")
                .content("content")
                .build();

        // when
        boardService.createBoard(request);

        // then
        List<Board> all = boardRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all).extracting("writer", "password", "title", "content")
                .containsExactlyInAnyOrder(tuple("yeop", "1234", "title", "content"));

    }

    @DisplayName("전체 게시물을 생성일자 내림차순으로 조회할 수 있다.")
    @Test
    void findAllBoard() throws Exception{
        // given
        boardRepository.save(makeBoard("yeop", "1234", "title1", "content1"));
        Thread.sleep(10);
        boardRepository.save(makeBoard("yeop", "1234", "title2", "content2"));
        Thread.sleep(10);
        boardRepository.save(makeBoard("yeop", "1234", "title3", "content3"));
        Thread.sleep(10);

        // when
        List<FindBoardResponse> allBoard = boardRepository.findAllBoard();

        // then
        assertThat(allBoard).extracting("writer", "title", "content")
                .containsExactly(
                        tuple("yeop", "title3", "content3")
                        , tuple("yeop", "title2", "content2")
                        , tuple("yeop", "title1", "content1"));
    }

    @DisplayName("전체 게시물을 조회할 대 게시물이 없는 경우 빈 리스트가 조회된다.")
    @Test
    void findAllBoardByEmpty() {

        // given // when
        List<FindBoardResponse> allBoard = boardService.findAllBoard();

        // then
        assertThat(allBoard).isEmpty();
    }

    @DisplayName("id로 게시물을 조회할 수 있다.")
    @Test
    void findBoardById() {
        // given
        Board saved = boardRepository.save(makeBoard("yeop", "1234", "title2", "content2"));
        Long id = saved.getId();
        // when
        FindBoardResponse boardById = boardService.findBoardById(id);

        // then
        assertThat(boardById).isNotNull();
        assertThat(boardById.getWriter()).isEqualTo("yeop");
        assertThat(boardById.getTitle()).isEqualTo("title2");
        assertThat(boardById.getContent()).isEqualTo("content2");
    }

    @DisplayName("id로 조회할 수 있는 게시물이 없는 경우 조회되지 않는다.")
    @Test
    void findBoardById_notFound() {
        // given
        Long id = 1L;

        // when // then
        assertThatThrownBy(() -> boardService.findBoardById(id)).isInstanceOf(EntityNotFoundException.class).hasMessage("조회된 게시물이 없습니다.");
    }

    private static Board makeBoard(String writer, String password, String title, String content) {
        return Board.builder().title(title).content(content).writer(writer).password(password).build();
    }

    @DisplayName("게시물을 수정할 수 있다.")
    @Test
    void modifyBoard() {
        // given
        Board saved = boardRepository.save(makeBoard("yeop", "1234", "title", "content"));
        Long id = saved.getId();
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("1234")
                .writer("yeop1")
                .title("changeTitle")
                .content("changeContent")
                .build();

        // when
        UpdateBoardResponse updateBoardResponse = boardService.updateBoard(id, request);

        // then
        assertThat(updateBoardResponse).isNotNull();
        assertThat(updateBoardResponse.getWriter()).isEqualTo("yeop1");
        assertThat(updateBoardResponse.getTitle()).isEqualTo("changeTitle");
        assertThat(updateBoardResponse.getContent()).isEqualTo("changeContent");
    }

    @DisplayName("게시물을 수정할 때 해당 게시물이 없는 경우 수정할 수 없다.")
    @Test
    void modityBoard_notFoundBoard() {
        // given
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("1234")
                .writer("yeop1")
                .title("changeTitle")
                .content("changeContent")
                .build();

        // when // then
        assertThatThrownBy(() -> boardService.updateBoard(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("조회된 게시물이 없습니다.");
    }

    @DisplayName("게시물을 수정할 때 비밀번호가 다른 경우 수정할 수 없다.")
    @Test
    void modifyBoard_isNotCorrectPassword() {
        // given
        Board saved = boardRepository.save(makeBoard("yeop", "1234", "title", "content"));
        Long id = saved.getId();
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("notPassword")
                .writer("yeop1")
                .title("changeTitle")
                .content("changeContent")
                .build();

        // when // then
        assertThatThrownBy(() -> boardService.updateBoard(id, request))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("비밀번호가 올바르지 않습니다.");

    }

    @DisplayName("게시물을 삭제할 수 있다.")
    @Test
    void deleteBoard() {
        // given
        Board saved = boardRepository.save(makeBoard("yeop", "1234", "title", "content"));
        Long id = saved.getId();

        DeleteBoardRequest request = DeleteBoardRequest.builder()
                .password("1234")
                .build();

        // when
        DeleteBoardResponse deleteBoardResponse = boardService.deleteBoard(id, request);

        // then
        assertThat(deleteBoardResponse).isNotNull();
    }

    @DisplayName("이미 삭제된 게시물인 경우에는 삭제하지 않고, 이전 삭제 시간을 반환한다.")
    @Test
    void deleteBoard_already() throws Exception{
        // given
        Board saved = boardRepository.save(makeBoard("yeop", "1234", "title", "content"));
        Long id = saved.getId();

        DeleteBoardRequest request = DeleteBoardRequest.builder()
                .password("1234")
                .build();

        DeleteBoardResponse deleteBoardResponse = boardService.deleteBoard(id, request);
        LocalDateTime deletedDatetime = deleteBoardResponse.getDeletedDatetime();
        Thread.sleep(10);

        // when
        DeleteBoardResponse alreadyDeleteResponse = boardService.deleteBoard(id, request);

        // then
        assertThat(deletedDatetime).isEqualTo(alreadyDeleteResponse.getDeletedDatetime());
    }

    @DisplayName("게시물을 삭제할 때 해당 게시물이 없는 경우 삭제할 수 없다.")
    @Test
    void deleteBoard_notFoundBoard() {
        // given
        DeleteBoardRequest request = DeleteBoardRequest.builder()
                .password("1234")
                .build();

        // when // then
        assertThatThrownBy(() -> boardService.deleteBoard(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("조회된 게시물이 없습니다.");
    }

    @DisplayName("게시물을 삭제할 때 비밀번호가 다른 경우 삭제할 수 없다.")
    @Test
    void deleteBoard_isNotCorrectPassword() {
        // given
        Board saved = boardRepository.save(makeBoard("yeop", "1234", "title", "content"));
        Long id = saved.getId();

        DeleteBoardRequest request = DeleteBoardRequest.builder()
                .password("password")
                .build();

        // when // then
        assertThatThrownBy(() -> boardService.deleteBoard(id, request))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("비밀번호가 올바르지 않습니다.");

    }

}