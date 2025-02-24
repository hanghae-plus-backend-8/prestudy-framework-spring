package hanghaeboard.api.service.member;

import hanghaeboard.api.service.member.response.FindMember;
import hanghaeboard.domain.member.Member;
import hanghaeboard.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("회원의 id로 회원을 조회할 수 있다.")
    @Test
    void findMemberById() {
        // given
        Member member = Member.builder().username("yeop").password("1234").build();
        Member save = memberRepository.save(member);
        Long id = save.getId();
        // when
        FindMember findMember = memberService.findMemberById(id);

        // then
        assertThat(findMember.getMemberId()).isEqualTo(id);
        assertThat(findMember.getUsername()).isEqualTo("yeop");
    }

    @DisplayName("id에 해당하는 회원이 없는 경우 조회할 수 없다.")
    @Test
    void findMemberByIdWithoutMember() {
        // given
        Long id = 1L;
        // when // then
        assertThatThrownBy(() -> memberService.findMemberById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조회된 회원이 없습니다.");
    }
}