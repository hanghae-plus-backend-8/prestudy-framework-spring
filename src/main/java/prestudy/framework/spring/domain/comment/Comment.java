package prestudy.framework.spring.domain.comment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.domain.BaseEntity;
import prestudy.framework.spring.domain.board.Board;
import prestudy.framework.spring.domain.user.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }

    public void updateContent(String content) {
        if (content == null || content.isBlank()) {
            return;
        }

        this.content = content;
    }

    public boolean hasNotWriterPermission(User user) {
        return isNotWriter(user) && user.isNotAdmin();
    }

    private boolean isNotWriter(User user) {
        return !this.user.getId().equals(user.getId());
    }
}
