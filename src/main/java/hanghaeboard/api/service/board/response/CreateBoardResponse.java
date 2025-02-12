package hanghaeboard.api.service.board.response;

import hanghaeboard.api.service.member.response.FindMember;
import hanghaeboard.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBoardResponse {

    private Long id;
    private FindMember member;
    private String title;
    private String content;

    @Builder
    private CreateBoardResponse(Long id, FindMember member, String title, String content) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public static CreateBoardResponse from(Board board) {
        return CreateBoardResponse.builder()
                .id(board.getId())
                .member(FindMember.of(board.getMember()))
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}
