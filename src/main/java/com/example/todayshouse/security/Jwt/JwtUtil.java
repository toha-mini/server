package com.example.todayshouse.security.Jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.*;

@Component
public class JwtUtil {

    private final String TOKEN_TYPE = "Bearer ";
    private final String HEADER_NAME = "Authorization";
    private final String AUTHORIZATION_KEY = "auth";

    private final Key secretKey;
    private final long TOKEN_EXPIRES_IN;

    public JwtUtil(
            @Value("${jwt.secret.key}") final String secretKey,
            @Value("${jwt.expire.length}") final long tokenExpiresIn
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.TOKEN_EXPIRES_IN = tokenExpiresIn;
    }

    public String createToken(String email) {
        final Date now = new Date();
        final Date validityTime = new Date(now.getTime() + TOKEN_EXPIRES_IN);

        return TOKEN_TYPE + Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validityTime)
                .signWith(secretKey, HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            return getClaims(token)
                    .getExpiration()
                    .after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }

    public String extractToken(String token) {
        if (isInvalidToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        return token.substring(TOKEN_TYPE.length());
    }

    private boolean isInvalidToken(String token) {
        return token == null || !token.startsWith(TOKEN_TYPE);
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void addTokenToHeader(String token, HttpServletResponse response) {
        response.setHeader(AUTHORIZATION_KEY, token);
    }

    public String getEmail(String token) {
        return getClaims(token)
                .getSubject();
    }

}
