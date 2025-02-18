package prestudy.framework.spring.api.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prestudy.framework.spring.api.jwt.JwtProvider;
import prestudy.framework.spring.api.service.user.command.UserCreateCommand;
import prestudy.framework.spring.api.service.user.command.UserLoginCommand;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public void createUser(UserCreateCommand createCommand) {
        userRepository.findByUsername(createCommand.getUsername())
            .ifPresent(user -> {
                throw new IllegalArgumentException("이미 존재하는 유저명입니다.");
            });

        userRepository.save(createCommand.toEntity());
    }

    public String loginUser(UserLoginCommand loginCommand) {
        User findUser = userRepository.findByUsername(loginCommand.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저명입니다."));

        if (findUser.isNotEqualPassword(loginCommand.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        return jwtProvider.generateToken(findUser.getId());
    }
}
