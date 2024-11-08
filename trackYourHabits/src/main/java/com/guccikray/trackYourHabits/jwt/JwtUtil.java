package com.guccikray.trackYourHabits.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, UUID userId) {
        UUID tokenUserId = UUID.fromString(extractUserId(token));
        return (userId.equals(tokenUserId) && !isTokenExpired(token));
    }

    public String extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
