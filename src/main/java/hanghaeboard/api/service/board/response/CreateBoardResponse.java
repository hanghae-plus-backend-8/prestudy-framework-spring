package hanghaeboard.api.service.board.response;

import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBoardResponse {
    private Long id;
    private Member member;
    private String title;
    private String content;

    @Builder
    private CreateBoardResponse(Long id, Member member, String title, String content) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public static CreateBoardResponse from(Board board) {
        return CreateBoardResponse.builder()
                .id(board.getId())
                .member(board.getMember())
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}
