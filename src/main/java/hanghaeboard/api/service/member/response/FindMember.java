package hanghaeboard.api.service.member.response;

import hanghaeboard.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindMember {

    private Long memberId;
    private String username;

    @Builder
    private FindMember(Long memberId, String username) {
        this.memberId = memberId;
        this.username = username;
    }

    public static FindMember of(Member member) {
        return FindMember.builder()
                .memberId(member.getId())
                .username(member.getUsername()).build();
    }
}
