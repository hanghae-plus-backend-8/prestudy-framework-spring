package hanghaeboard.api.service.board.request;

import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBoardRequest {

    private Member member;
    private String title;
    private String content;

    @Builder
    private CreateBoardRequest(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public Board toEntity(){
        return Board.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();
    }
}
