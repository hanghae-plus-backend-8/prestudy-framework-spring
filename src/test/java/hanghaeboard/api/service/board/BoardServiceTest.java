package hanghaeboard.api.service.board;

import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.board.BoardRepository;
import hanghaeboard.domain.member.Member;
import hanghaeboard.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
        Member member = Member.builder().username("yeop").password("1234").build();
        memberRepository.save(member);

        CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .content("content")
                .build();

        // when
        boardService.createBoard(request);

        // then
        List<Board> all = boardRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all).extracting("title", "content")
                .containsExactlyInAnyOrder(tuple("title", "content"));
        assertThat(all.get(0).getMember().getUsername()).isEqualTo("yeop");
    }

    @DisplayName("게시물을 생성할 때 회원 정보가 정확하지 않은 경우 게시물을 생성할 수 없다.")
    @Test
    void createBoardWithoutMember() {
        // given

        CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(1L)
                .title("title")
                .content("content")
                .build();

        // when // then
        assertThatThrownBy(
                ()-> boardService.createBoard(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조회된 회원이 없습니다.");
    }

}