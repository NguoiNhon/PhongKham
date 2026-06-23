package com.local.PhongKham.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey123";

    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // tạo token
    public String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 86400000)
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // lấy toàn bộ claims
    public Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // lấy username
    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // lấy role
    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    // validate token
    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}