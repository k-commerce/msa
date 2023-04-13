package com.kcommerce.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;

    public JwtProvider(@Value("${jwt.key}") String key) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public String createJwt(String sub, long time) {
        return Jwts.builder()
                .setSubject(sub)
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(key)
                .compact();
    }

    public String getSubject(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }
}
