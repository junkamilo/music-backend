package com.musicplatform.backend_core.shared.util;

import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.config.JwtProperties;
import com.musicplatform.backend_core.shared.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtProperties.getAccessExpirationMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(String.valueOf(UserGetters.getId(user)))
                .claim("role", UserGetters.getRole(user).name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public long getAccessExpirationSeconds() {
        return jwtProperties.getAccessExpirationMinutes() * 60L;
    }

    public JwtUserClaims parseAndValidate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Long userId = Long.parseLong(claims.getSubject());
            UserRole role = UserRole.valueOf(claims.get("role", String.class));
            return new JwtUserClaims(userId, role);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new JwtException("Token inválido", ex);
        }
    }

    public record JwtUserClaims(Long userId, UserRole role) {
    }
}
