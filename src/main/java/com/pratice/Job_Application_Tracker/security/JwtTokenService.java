package com.pratice.Job_Application_Tracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {
    private final SecretKey signingKey;
    private final long expirationMillis;

    public JwtTokenService(@Value("${app.jwt.secret}") String base64Secret,
        @Value("${app.jwt.expiration-ms}") long expirationMillis) {
        if (base64Secret == null || base64Secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET must be set to a Base64-encoded key of at least 256 bits.");
        }
        byte[] keyBytes;
        try { keyBytes = Decoders.BASE64.decode(base64Secret); }
        catch (RuntimeException exception) { throw new IllegalStateException("JWT_SECRET must be valid Base64.", exception); }
        if (keyBytes.length < 32) throw new IllegalStateException("JWT_SECRET must decode to at least 32 bytes.");
        if (expirationMillis <= 0) throw new IllegalStateException("JWT_EXPIRATION_MS must be positive.");
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMillis = expirationMillis;
    }

    public String issueToken(String email) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expirationMillis);
        return Jwts.builder().subject(email).issuedAt(issuedAt).expiration(expiresAt)
            .signWith(signingKey).compact();
    }

    public String extractEmail(String token) { return claims(token).getSubject(); }
    public boolean isValid(String token) {
        try { return claims(token).getSubject() != null; }
        catch (RuntimeException exception) { return false; }
    }
    public long getExpirationMillis() { return expirationMillis; }
    private Claims claims(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }
}
