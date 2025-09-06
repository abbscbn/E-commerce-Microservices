package com.abbas.ecommerce.order.jwt;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;



    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes()); // güvenli key objesi
       try{
           return Jwts.parserBuilder()
                   .setSigningKey(key)  // secret string değil, Key objesi
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
       }
       catch (ClaimJwtException e){
        return  e.getClaims();
       }
    }
}
