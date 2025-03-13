package prestudy.framework.spring.domain.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.domain.BaseEntity;
import prestudy.framework.spring.domain.user.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void updateTitle(String title) {
        if (title == null || title.isBlank()) {
            return;
        }

        this.title = title;
    }

    public void updateContent(String content) {
        if (content == null || content.isBlank()) {
            return;
        }

        this.content = content;
    }

    public boolean isNotWriter(User user) {
        return !this.user.getId().equals(user.getId());
    }
}
