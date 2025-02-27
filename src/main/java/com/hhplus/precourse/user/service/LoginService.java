package com.hhplus.precourse.user.service;

import com.hhplus.precourse.common.exception.BadRequestException;
import com.hhplus.precourse.common.exception.NotFoundException;
import com.hhplus.precourse.user.repository.UserRepository;
import com.hhplus.precourse.common.component.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.MISMATCH_PASSWORD;
import static com.hhplus.precourse.common.support.ApplicationStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;

    @Transactional(readOnly = true)
    public String login(Command command) {
        var user = userRepository.findByName(command.name())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        if (user.notMatchPassword(command.password())) {
            throw new BadRequestException(MISMATCH_PASSWORD);
        }

        return jwtTokenManager.issue(user.id(), user.name());
    }

    public record Command(
        String name,
        String password
    ) {
    }
} 