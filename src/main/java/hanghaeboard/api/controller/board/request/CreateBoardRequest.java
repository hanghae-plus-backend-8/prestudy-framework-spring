package hanghaeboard.api.controller.board.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import hanghaeboard.api.service.board.request.CreateBoardServiceRequest;
import hanghaeboard.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBoardRequest {

    private Long memberId;
    private String title;
    private String content;

    @Builder
    private CreateBoardRequest(Long memberId, String title, String content) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
    }

    public CreateBoardServiceRequest to(Member member) {
        return CreateBoardServiceRequest.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();
    }
}
