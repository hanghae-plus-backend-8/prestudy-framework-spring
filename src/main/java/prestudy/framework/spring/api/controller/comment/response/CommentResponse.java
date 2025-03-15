package prestudy.framework.spring.api.controller.comment.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.domain.comment.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private LocalDateTime createdDate;

    @Builder
    private CommentResponse(Long id, String content, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
    }

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .createdDate(comment.getCreatedDateTime())
            .build();
    }
}
