package hanghaeboard.domain.board;

import hanghaeboard.domain.member.Member;
import hanghaeboard.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @DisplayName("게시판을 생성할 수 있다.")
    @Test
    void createBoard() {
        // given
        Member member = Member.builder().username("yeop").password("1234").build();
        Member save = memberRepository.save(member);
        Board board = Board.builder().member(save).title("hi").content("hihihi").build();

        // when
        boardRepository.save(board);

        // then
        List<Board> all = boardRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(tuple("hi", "hihihi"));
        assertThat(all.get(0).getMember().getUsername()).isEqualTo("yeop");
    }
}