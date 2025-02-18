package prestudy.framework.spring.api.service.user;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.api.jwt.JwtProvider;
import prestudy.framework.spring.api.service.user.command.UserCreateCommand;
import prestudy.framework.spring.api.service.user.command.UserLoginCommand;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("이미 존재하는 유저명으로 유저를 생성하지 못한다.")
    @Test
    void createUserWithExistUsername() {
        // given
        String username = "123abc";
        String password = "Password12";

        userRepository.save(User.of(username, password));

        UserCreateCommand createCommand = UserCreateCommand.builder()
            .username(username)
            .password(password)
            .build();

        // when & then
        assertThatThrownBy(() -> userService.createUser(createCommand))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 존재하는 유저명입니다.");
    }

    @DisplayName("유저를 생성한다.")
    @Test
    void createUser() {
        // given
        String username = "123abc";
        String password = "Password12";

        UserCreateCommand createCommand = UserCreateCommand.builder()
            .username(username)
            .password(password)
            .build();

        // when
        userService.createUser(createCommand);

        // then
        Optional<User> saveUser = userRepository.findByUsername(username);
        assertThat(saveUser).isNotEmpty();
        assertThat(saveUser.get().getUsername()).isEqualTo(username);
        assertThat(saveUser.get().getPassword()).isEqualTo(password);
    }

    @DisplayName("존재하지 않는 유저명이면 로그인하지 못한다.")
    @Test
    void loginWithNotExistUsername() {
        // given
        UserLoginCommand loginCommand = UserLoginCommand.builder()
            .username("123abc")
            .password("<PASSWORD>")
            .build();

        // when & then
        assertThatThrownBy(() -> userService.loginUser(loginCommand))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 유저명입니다.");
    }

    @DisplayName("패스워드가 일치하지 않으면 로그인하지 못한다.")
    @Test
    void loginWithWrongPassword() {
        // given
        String username = "123abc";
        String password = "Password12";

        userRepository.save(User.of(username, password));

        UserLoginCommand loginCommand = UserLoginCommand.builder()
            .username("123abc")
            .password("<PASSWORD>")
            .build();

        // when & then
        assertThatThrownBy(() -> userService.loginUser(loginCommand))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("패스워드가 일치하지 않습니다.");
    }

    @DisplayName("로그인이 성공하면 JWT를 반환한다.")
    @Test
    void loginUserWithReturnJwt() {
        // given
        String username = "123abc";
        String password = "Password12";

        User savedUser = userRepository.save(User.of(username, password));

        UserLoginCommand loginCommand = UserLoginCommand.builder()
            .username("123abc")
            .password("Password12")
            .build();

        // when
        String jwt = userService.loginUser(loginCommand);

        // then
        assertThat(jwt).isNotBlank();

        Claims claims = jwtProvider.parseToken(jwt);
        assertThat(claims.getSubject()).isEqualTo(savedUser.getId().toString());
        assertThat(claims.getExpiration()).isAfter(new Date());
    }
}