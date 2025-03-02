package prestudy.framework.spring.api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtProviderTest extends IntegrationTestSupport {

    @Autowired
    private JwtProvider jwtProvider;

    @DisplayName("JWT 토큰을 유저ID와 만료 시간으로 생성한다.")
    @Test
    void generateToken() {
        // given
        Long userId = 1L;
        LocalDateTime expiration = LocalDateTime.of(2025, 2, 7, 12, 0);

        // when
        String jwt = jwtProvider.generateToken(userId, expiration);

        // then
        assertThat(jwt).isNotBlank();
    }

    @DisplayName("JWT 토큰이 만료되면 파싱에 실패한다.")
    @Test
    void parseTokenWithExpired() {
        // given
        Long userId = 1L;
        LocalDateTime expiration = LocalDateTime.of(2025, 2, 7, 12, 0);

        String jwt = jwtProvider.generateToken(userId, expiration);

        // when & then
        assertThatThrownBy(() -> jwtProvider.parseToken(jwt))
            .isInstanceOf(ExpiredJwtException.class);
    }

    @DisplayName("JWT 토큰을 파싱한다.")
    @Test
    void parseToken() {
        // given
        Long userId = 1L;
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        String jwt = jwtProvider.generateToken(userId, expiration);

        // when
        Claims claims = jwtProvider.parseToken(jwt);

        // then
        assertThat(claims.getSubject()).isEqualTo(userId.toString());
        assertThat(claims.getExpiration()).isAfter(new Date());
    }
}