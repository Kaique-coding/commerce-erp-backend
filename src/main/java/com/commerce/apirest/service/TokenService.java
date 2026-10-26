
package com.commerce.apirest.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    private final SecretKey signingKey;
    private final String issuer;
    private final String audience;
    private final long expirationMinutes;

    public TokenService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.audience}") String audience,
            @Value("${security.jwt.expiration-minutes:15}")
            long expirationMinutes) {

        if (!StringUtils.hasText(secret)
                || !StringUtils.hasText(issuer)
                || !StringUtils.hasText(audience)) {
            throw new IllegalArgumentException(
                    "A configuração JWT está incompleta.");
        }

        if (expirationMinutes <= 0) {
            throw new IllegalArgumentException(
                    "O tempo de expiração deve ser positivo.");
        }

        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "A chave JWT deve ser Base64 válida "
                    + "e possuir pelo menos 256 bits.", exception);
        }

        this.issuer = issuer;
        this.audience = audience;
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(String subject) {
        if (!StringUtils.hasText(subject)) {
            throw new IllegalArgumentException(
                    "O identificador do usuário é obrigatório.");
        }

        Instant now = Instant.now();
        Instant expiration = now.plus(
                expirationMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(subject)
                .audience()
                    .add(audience)
                    .and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        try {
            Claims claims = parseClaims(token);

            return StringUtils.hasText(claims.getSubject())
                    && claims.getIssuedAt() != null
                    && claims.getExpiration() != null
                    && claims.getExpiration()
                            .after(new Date())
                    && claims.getId() != null;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean validateToken(
            String token, String expectedSubject) {

        if (!StringUtils.hasText(expectedSubject)
                || !validateToken(token)) {
            return false;
        }

        try {
            return Objects.equals(
                    parseClaims(token).getSubject(),
                    expectedSubject);
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Instant extractExpiration(String token) {
        return parseClaims(token)
                .getExpiration()
                .toInstant();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(issuer)
                .requireAudience(audience)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
