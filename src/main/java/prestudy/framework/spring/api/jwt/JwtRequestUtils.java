package prestudy.framework.spring.api.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
public class JwtRequestUtils {

    public Long getUserId() {
        HttpServletRequest request = getCurrentRequest();
        Object userId = request.getAttribute("userId");

        if (userId == null) {
            log.error("userId is null");
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
