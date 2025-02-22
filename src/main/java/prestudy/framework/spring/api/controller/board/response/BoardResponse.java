package prestudy.framework.spring.api.controller.board.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.domain.board.Board;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdDate;

    @Builder
    private BoardResponse(Long id, String title, String content, String writer, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdDate = createdDate;
    }

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
            .id(board.getId())
            .title(board.getTitle())
            .content(board.getContent())
            .writer(board.getWriter())
            .createdDate(board.getCreatedDate())
            .build();
    }
}
