package hanghaeboard.api.service.member;

import hanghaeboard.domain.member.Member;
import hanghaeboard.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberById(Long id){
        return memberRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("조회된 회원이 없습니다."));
    }

}
