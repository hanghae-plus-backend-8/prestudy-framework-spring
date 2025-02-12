package hanghaeboard.domain.board;


import hanghaeboard.domain.BaseEntity;
import hanghaeboard.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @Builder
    private Board(Long id, Member member, String title, String content) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
    }
}
