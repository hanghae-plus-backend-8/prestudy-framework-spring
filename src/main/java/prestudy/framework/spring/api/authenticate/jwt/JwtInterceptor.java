package prestudy.framework.spring.api.authenticate.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import prestudy.framework.spring.api.authenticate.AuthenticationHandler;
import prestudy.framework.spring.api.authenticate.AuthenticationInterceptor;
import prestudy.framework.spring.api.exception.AuthenticationException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements AuthenticationInterceptor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final AuthenticationHandler authenticationHandler;
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (authenticationHandler.isIgnoreAuthentication(handler)) {
            return true;
        }

        try {
            String header = getAuthorizationHeaderBy(request);
            Claims claims = jwtProvider.verify(header);

            String subject = claims.getSubject();
            Long userId = Long.parseLong(subject);

            request.setAttribute("userId", userId);
        } catch (MalformedJwtException | ExpiredJwtException e) {
            throw new AuthenticationException(e.getMessage());
        }

        return true;
    }

    private String getAuthorizationHeaderBy(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (header == null) {
            throw new AuthenticationException("Authorization header is null");
        }

        if (isNotBearerStartsWith(header)) {
            throw new AuthenticationException("Authorization header is not Bearer starts with");
        }

        return header.substring(BEARER_TOKEN_PREFIX.length());
    }

    private boolean isNotBearerStartsWith(String header) {
        return !header.startsWith(BEARER_TOKEN_PREFIX);
    }
}
