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
    public FindMember(Long memberId, String username) {
        this.memberId = memberId;
        this.username = username;
    }

    public static FindMember of(Member member) {
        return new FindMember(member.getId(), member.getUsername());
    }
}
