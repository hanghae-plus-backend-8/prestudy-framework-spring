package hanghaeboard.api.service.board.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FindBoardResponse {

    private Long id;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDatetime;
    private LocalDateTime lastModifiedDatetime;

    @Builder
    public FindBoardResponse(Long id, String writer, String title, String content, LocalDateTime createdDatetime, LocalDateTime lastModifiedDatetime) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdDatetime = createdDatetime;
        this.lastModifiedDatetime = lastModifiedDatetime;
    }
}
