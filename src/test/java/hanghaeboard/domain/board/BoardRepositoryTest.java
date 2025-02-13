package hanghaeboard.domain.board;

import hanghaeboard.api.service.board.response.FindBoardResponse;
import hanghaeboard.config.AuditingConfig;
import hanghaeboard.domain.member.Member;
import hanghaeboard.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@ActiveProfiles("test")
@Import(AuditingConfig.class)
class BoardRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("게시판을 생성할 수 있다.")
    @Test
    void createBoard() {
        // given
        Member member = Member.builder().username("yeop").password("1234").build();
        Member savedMember = memberRepository.save(member);
        Board board = makeBoard(savedMember, "hi", "hihihi");


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

    private static Board makeBoard(Member member, String title, String content) {
        return Board.builder().member(member).title(title).content(content).build();
    }

    @DisplayName("생성 일자로 정렬된 게시물 목록을 조회할 수 있다.")
    @Test
    void findBoardList() throws Exception{
        // given
        Member member = Member.builder().username("yeop").password("1234").build();
        Member savedMember = memberRepository.save(member);
        Board board1 = makeBoard(savedMember, "title1", "content1");
        Thread.sleep(10);
        Board board2 = makeBoard(savedMember, "title2", "content2");
        Thread.sleep(10);
        Board board3 = makeBoard(savedMember, "title3", "content3");

        boardRepository.saveAll(List.of(board1, board2, board3));
        
        // when
        List<Board> allWithMember = boardRepository.findAllWithMember();

        // then
        assertThat(allWithMember).extracting("title", "content","member.username")
                .containsExactly(
                        tuple("title3", "content3", "yeop"),
                        tuple("title2", "content2", "yeop"),
                        tuple("title1", "content1", "yeop"));
    }

    @DisplayName("생성 일자로 정렬된 게시물 목록을 조회할 수 있다.")
    @Test
    void findAllBoard() throws Exception {
        // given
        Member member = Member.builder().username("yeop").password("1234").build();
        Member savedMember = memberRepository.save(member);
        Board board1 = makeBoard(savedMember, "title1", "content1");
        Thread.sleep(10);
        Board board2 = makeBoard(savedMember, "title2", "content2");
        Thread.sleep(10);
        Board board3 = makeBoard(savedMember, "title3", "content3");

        boardRepository.saveAll(List.of(board1, board2, board3));

        // when
        List<FindBoardResponse> allBoard = boardRepository.findAllBoard();

        // then
    }
}