package hanghaeboard.api.service.board;

import hanghaeboard.api.service.board.request.CreateBoardRequest;
import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.board.BoardRepository;
import hanghaeboard.domain.member.Member;
import hanghaeboard.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("게시물을 생성할 수 있다.")
    @Test
    void createBoard() {
        // given
        Member member = Member.builder().username("yeop").password("1234").build();
        memberRepository.save(member);

        CreateBoardRequest request = CreateBoardRequest.builder()
                .member(member)
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

}