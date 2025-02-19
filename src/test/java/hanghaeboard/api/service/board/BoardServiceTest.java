package hanghaeboard.api.service.board;

import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.board.BoardRepository;
import hanghaeboard.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
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

    private static Board makeBoard(String writer, String password, String title, String content) {
        return Board.builder().title(title).content(content).writer(writer).password(password).build();
    }

}