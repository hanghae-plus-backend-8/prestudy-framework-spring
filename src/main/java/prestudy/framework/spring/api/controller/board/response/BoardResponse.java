package prestudy.framework.spring.api.controller.board.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.api.controller.comment.response.CommentResponse;
import prestudy.framework.spring.domain.board.Board;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdDate;
    private List<CommentResponse> comments;

    @Builder
    private BoardResponse(Long id,
                          String title,
                          String content,
                          String writer,
                          LocalDateTime createdDate,
                          List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdDate = createdDate;
        this.comments = comments;
    }

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
            .id(board.getId())
            .title(board.getTitle())
            .content(board.getContent())
            .writer(board.getUser().getUsername())
            .createdDate(board.getCreatedDateTime())
            .build();
    }

    public static BoardResponse of(Board board, List<CommentResponse> comments) {
        return BoardResponse.builder()
            .id(board.getId())
            .title(board.getTitle())
            .content(board.getContent())
            .writer(board.getUser().getUsername())
            .createdDate(board.getCreatedDateTime())
            .comments(comments)
            .build();
    }
}
