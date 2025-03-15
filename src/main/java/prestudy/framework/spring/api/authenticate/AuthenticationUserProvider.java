package prestudy.framework.spring.api.authenticate;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
            .orElseThrow(() -> new IllegalStateException("토큰이 유효하지 않습니다."));
    }

    private Long getUserId() {
        HttpServletRequest request = getCurrentRequest();
        Object userId = request.getAttribute("userId");

        if (userId == null) {
            log.error("UserId is null");
            throw new IllegalStateException("토큰이 유효하지 않습니다.");
        }

        return (Long) userId;
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            log.error("No request attributes found");
            throw new IllegalStateException("토큰이 유효하지 않습니다.");
        }

        return attributes.getRequest();
    }
}
