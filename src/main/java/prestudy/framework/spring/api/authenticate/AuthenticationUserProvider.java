package prestudy.framework.spring.api.authenticate;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import prestudy.framework.spring.api.exception.AuthenticationException;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationUserProvider {

    private final UserRepository userRepository;

    public User authenticatedUser() {
        Long userId = getUserId();

        return userRepository.findById(userId)
            .orElseThrow(() -> new AuthenticationException("사용자가 올바르지 않습니다."));
    }

    private Long getUserId() {
        HttpServletRequest request = getCurrentRequest();
        Object userId = request.getAttribute("userId");

        if (userId == null) {
            throw new AuthenticationException("사용자 ID가 존재하지 않습니다.");
        }

        return (Long) userId;
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new AuthenticationException("요청이 올바르지 않습니다.");
        }

        return attributes.getRequest();
    }
}
