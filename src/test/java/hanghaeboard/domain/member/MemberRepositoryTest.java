package hanghaeboard.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @DisplayName("회원을 저장할 수 있다.")
    @Test
    void saveMember() {
        // given
        Member member = Member.builder().username("이건엽").password("12345678").build();

        // when
        memberRepository.save(member);

        // then
        List<Member> all = memberRepository.findAll();

        assertThat(all).hasSize(1);
        Member findMember = all.get(0);
        assertThat(findMember.getId()).isEqualTo(1L);
        assertThat(findMember.getUsername()).isEqualTo("이건엽");
        assertThat(findMember.getPassword()).isEqualTo("12345678");
    }

    @DisplayName("username으로 회원을 조회할 수 있다.")
    @Test
    void findByUsername() {
        // given
        Member member = Member.builder().username("yeop").password("12345678").build();
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findByUsername("yeop");

        // then
        assertThat(findMember.getUsername()).isEqualTo("yeop");
    }
}