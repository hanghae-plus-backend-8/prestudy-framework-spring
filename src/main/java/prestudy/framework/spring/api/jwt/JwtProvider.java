package prestudy.framework.spring.api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.provider.secret-key}")
    private String secretKey;

    @Value("${jwt.provider.access-token-expiration}")
    private Long accessTokenExpiration;

    public String generateToken(Long userId) {
        return generateToken(userId, LocalDateTime.now().plusSeconds(accessTokenExpiration));
    }

    public String generateToken(Long userId, LocalDateTime expiration) {
        return Jwts.builder()
            .subject(userId.toString())
            .expiration(convertToDate(expiration))
            .signWith(createSecretKey())
            .compact();
    }

    public Claims parseToken(String jwt) {
        return Jwts.parser()
            .verifyWith(createSecretKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
    }

    private SecretKey createSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Date convertToDate(LocalDateTime expiration) {
        return Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
    }
}
