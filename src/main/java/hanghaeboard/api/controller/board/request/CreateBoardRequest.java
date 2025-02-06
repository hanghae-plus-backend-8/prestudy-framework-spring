package hanghaeboard.api.controller.board.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import hanghaeboard.api.service.board.request.CreateBoardServiceRequest;
import hanghaeboard.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBoardRequest {


    @NotNull(message = "회원 정보가 없습니다.")
    private Long memberId;

    @NotBlank(message = "내용은 필수 입력입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력입니다.")
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
