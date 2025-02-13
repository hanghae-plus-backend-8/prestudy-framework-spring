package prestudy.framework.spring.api.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prestudy.framework.spring.api.service.user.command.UserCreateCommand;
import prestudy.framework.spring.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserCreateCommand createCommand) {
        userRepository.findByUsername(createCommand.getUsername())
            .ifPresent(user -> {
                throw new IllegalArgumentException("이미 존재하는 유저명입니다.");
            });

        userRepository.save(createCommand.toEntity());
    }
}
