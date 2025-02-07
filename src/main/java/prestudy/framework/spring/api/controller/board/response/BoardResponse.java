package prestudy.framework.spring.api.controller.board.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.domain.board.Board;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponse {

    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdDate;

    @Builder
    private BoardResponse(String title, String content, String writer, LocalDateTime createdDate) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdDate = createdDate;
    }

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
            .title(board.getTitle())
            .content(board.getContent())
            .writer(board.getWriter())
            .createdDate(board.getCreatedDate())
            .build();
    }
}
