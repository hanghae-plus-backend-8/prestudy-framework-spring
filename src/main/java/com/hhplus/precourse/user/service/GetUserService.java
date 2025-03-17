package com.hhplus.precourse.user.service;

import com.hhplus.precourse.common.exception.NotFoundException;
import com.hhplus.precourse.user.repository.UserRepository;
import com.hhplus.precourse.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GetUserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserVo get(long userId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return UserVo.from(user);
    }
}