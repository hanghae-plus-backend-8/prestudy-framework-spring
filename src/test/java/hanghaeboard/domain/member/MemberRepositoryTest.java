package hanghaeboard.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원을 저장할 수 있다.")
    @Test
    void saveMember() {
        // given
        Member member = Member.builder().username("이건엽").password("1234").build();

        // when
        memberRepository.save(member);

        // then
        List<Member> all = memberRepository.findAll();

        assertThat(all).hasSize(1);
        Member findMember = all.get(0);
        assertThat(findMember.getId()).isEqualTo(1L);
        assertThat(findMember.getUsername()).isEqualTo("이건엽");
        assertThat(findMember.getPassword()).isEqualTo("1234");

    }
}