package hanghaeboard.domain.board;


import hanghaeboard.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String writer;

    private String password;

    private String title;

    private String content;

    @Builder
    private Board(Long id, String writer, String password, String title, String content, LocalDateTime createdDatetime) {
        this.id = id;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
    }
}
