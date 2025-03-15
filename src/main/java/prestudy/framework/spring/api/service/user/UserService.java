package prestudy.framework.spring.api.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prestudy.framework.spring.api.authenticate.jwt.JwtProvider;
import prestudy.framework.spring.api.service.user.command.UserCreateCommand;
import prestudy.framework.spring.api.service.user.command.UserLoginCommand;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public void createUser(UserCreateCommand createCommand) {
        userRepository.findByUsername(createCommand.getUsername())
            .ifPresent(user -> {
                throw new IllegalArgumentException("중복된 username 입니다.");
            });

        userRepository.save(createCommand.toEntity());
    }

    @Transactional(readOnly = true)
    public String loginUser(UserLoginCommand loginCommand) {
        User findUser = userRepository.findByUsername(loginCommand.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        if (findUser.isNotEqualPassword(loginCommand.getPassword())) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        return jwtProvider.generateToken(findUser.getId());
    }
}
