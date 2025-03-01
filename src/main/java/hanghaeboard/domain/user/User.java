package hanghaeboard.domain.user;

import hanghaeboard.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String password;

    @Builder
    private User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public boolean isCorrectPassword(String password){
        return this.password.equals(password);
    }
}
