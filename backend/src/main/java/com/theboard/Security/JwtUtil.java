package com.theboard.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private long EXPIRATION;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {

        return Jwts.builder()

                .setSubject(email)

                .setIssuedAt(new Date())

                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))

                .signWith(getSigningKey(), SignatureAlgorithm.HS256)

                .compact();
    }

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();

    }

    public Date extractExpiration(String token) {

        return extractClaims(token).getExpiration();

    }

    public Claims extractClaims(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getSigningKey())

                .build()

                .parseClaimsJws(token)

                .getBody();

    }

    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    public boolean validateToken(String token, String email) {

        String username = extractUsername(token);

        return username.equals(email) && !isTokenExpired(token);

    }

}