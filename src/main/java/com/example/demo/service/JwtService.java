package com.example.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.model.TokenModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String accessSecretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.secret-refresh}")
    private String refreshSecretKey;
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(accessSecretKey.getBytes());
    }

    private SecretKey getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
    }

    public String generateAccessToken(TokenModel tokenModel) {
        return Jwts.builder()
                .subject(tokenModel.getEmail())
                .claim("name", tokenModel.getName())
                .claim("id", tokenModel.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(TokenModel tokenModel) {
        return Jwts.builder()
                .subject(tokenModel.getEmail())
                .claim("name", tokenModel.getName())
                .claim("id", tokenModel.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime * 7)) // Refresh token valid for 7 days
                .signWith(getRefreshSigningKey())
                .compact();
    }

    private TokenModel extractClaims(String token) {
        try {
            
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    
            TokenModel tokenModel = new TokenModel();
            tokenModel.setName(claims.get("name", String.class));
            tokenModel.setId(claims.get("id", String.class));
            tokenModel.setEmail(claims.getSubject());
            return tokenModel;
        } catch (Exception e) {
            return null; // Return null if token is invalid or expired
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            
            Date expiration = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            // TODO: handle exception
            return true; // Return true if token is invalid or expired
        }
    }

    private TokenModel extractRefreshClaims(String token) {
        try {
            
            Claims claims = Jwts.parser()
                    .verifyWith(getRefreshSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    
            TokenModel tokenModel = new TokenModel();
            tokenModel.setName(claims.get("name", String.class));
            tokenModel.setId(claims.get("id", String.class));
            tokenModel.setEmail(claims.getSubject());
            return tokenModel;
        } catch (Exception e) {
            // TODO: handle exception
            return null; // Return null if token is invalid or expired
        }
    }

    private boolean isTokenRefreshExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(getRefreshSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true; // Return true if token is invalid or expired
        }
    }

    public TokenModel isTokenValid(String token) {
        if (extractClaims(token) != null && !isTokenExpired(token)) {
            return extractClaims(token);
        }
        return null;
    }

    public TokenModel isRefreshTokenValid(String token) {
        if (extractRefreshClaims(token) != null && !isTokenRefreshExpired(token)) {
            return extractRefreshClaims(token);
        }
        return null;
    }
}