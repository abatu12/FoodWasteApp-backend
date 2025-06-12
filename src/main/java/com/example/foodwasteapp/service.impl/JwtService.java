package com.example.foodwasteapp.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET =
            "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    private final Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    public static final long ACCESS_MS  = 5 * 60 * 1000;          // 5 minuta
    public static final long REFRESH_MS = 7L * 24 * 60 * 60 * 1000; // 7 dana


    public String generateAccessToken(String username, String role) {
        return createToken(Map.of("role", role), username, ACCESS_MS);
    }

    public String generateRefreshToken(String username) {
        return createToken(Map.of(), username, REFRESH_MS);
    }

    private String createToken(Map<String,Object> claims,
                               String subject,
                               long ttlMillis) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ttlMillis))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token,
                              Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractRole(String token) {
        return extractClaim(token, c -> c.get("role", String.class));
    }


    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !extractExpiration(token).before(new Date());
    }

    public String getUsername(String token) {
        return extractUsername(token);
    }
    public String getRole(String token) {
        return extractRole(token);
    }
}
