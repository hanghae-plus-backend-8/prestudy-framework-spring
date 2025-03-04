package prestudy.framework.spring.domain.board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prestudy.framework.spring.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String writer;

    private String password;

    @Builder
    private Board(String title, String content, String writer, String password) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
    }

    public boolean isInvalidPassword(String password) {
        return !this.password.equals(password);
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

    public void updateWriter(String writer) {
        if (writer == null || writer.isBlank()) {
            return;
        }

        this.writer = writer;
    }
}
