package prestudy.framework.spring.domain.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("존재하지 않는 유저명으로 유저를 가져온다.")
    @Test
    void findByNotExistUsername() {
        // given
        String username = "123abc";

        // when
        Optional<User> notExistUser = userRepository.findByUsername(username);

        // then
        assertThat(notExistUser).isEmpty();
    }

    @DisplayName("존재하는 유저명으로 유저를 가져온다.")
    @Test
    void findByExistUsername() {
        // given
        String username = "123abc";
        String password = "Password12";

        userRepository.save(User.of(username, password));

        // when
        Optional<User> user = userRepository.findByUsername(username);

        // then
        assertThat(user).isNotEmpty();
        assertThat(user.get().getUsername()).isEqualTo(username);
        assertThat(user.get().getPassword()).isEqualTo(password);
    }

}