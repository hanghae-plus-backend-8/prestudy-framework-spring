package hanghaeboard.api.service.board.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FindBoardResponse {

    private Long boardId;
    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime createdDatetime;
    private LocalDateTime lastModifiedDatetime;

    public FindBoardResponse(Long boardId, Long memberId, String title, String content, LocalDateTime createdDatetime, LocalDateTime lastModifiedDatetime) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdDatetime = createdDatetime;
        this.lastModifiedDatetime = lastModifiedDatetime;
    }
}
