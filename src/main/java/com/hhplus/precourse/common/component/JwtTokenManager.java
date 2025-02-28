package com.hhplus.precourse.common.component;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenManager {
    private final Key key;
    private final long validityInMilliseconds;

    public JwtTokenManager(@Value("${application.jwt.secret}") String secretKey,
                           @Value("${application.jwt.expiration}") long validityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String issue(long userId, String userName) {
        var claims = Jwts.claims().setSubject(userName);
        claims.put("uid", userId);

        var issuedAt = new Date();
        var validUntil = new Date(issuedAt.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(validUntil)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
} 