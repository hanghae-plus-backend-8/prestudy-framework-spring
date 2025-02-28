package com.hhplus.precourse.user.service;

import com.hhplus.precourse.common.exception.BadRequestException;
import com.hhplus.precourse.user.domain.User;
import com.hhplus.precourse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.ALREADY_EXIST_USER;

@Service
@RequiredArgsConstructor
public class CreateUserService {
    private final UserRepository userRepository;

    @Transactional
    public void create(Command command) {
        if (userRepository.existsByName(command.name())) {
            throw new BadRequestException(ALREADY_EXIST_USER);
        }

        var user = createUser(command);

        userRepository.save(user);
    }

    private User createUser(Command command) {
        return new User(
            command.name(),
            command.password()
        );
    }

    public record Command(
        String name,
        String password
    ) {
    }
}
