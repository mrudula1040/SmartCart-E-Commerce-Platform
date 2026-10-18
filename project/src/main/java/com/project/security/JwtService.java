package com.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
private final SecretKey secretKey;
private final long expirationTime;

public JwtService(@Value("${jwt.secret}")String secret, @Value("${jwt.expiration-ms:3600000}") long expirationTime){
this.secretKey=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    this.expirationTime=expirationTime;
}

    public String generateToken(String email, String role) {

        Date now = new Date();

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime()+ expirationTime))
                .signWith(secretKey)
                .compact();
    }
    private Claims parseClaims(String token){
    return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String extractEmail(String token) {
    return  parseClaims(token).getSubject();
    }
    public String extractRole(String token) {
    return parseClaims(token).get("role",String.class);
    }

    public boolean isTokenValid(String token) {

        try {
            parseClaims(token);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}