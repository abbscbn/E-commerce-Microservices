package com.abbas.ecommerce.identity.jwt;

import com.abbas.ecommerce.identity.enumtype.Role;
import io.jsonwebtoken.ClaimJwtException;
import org.springframework.stereotype.Component;

// Spring anotasyonları
import org.springframework.beans.factory.annotation.Value;


// JWT kütüphanesi
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Java standart kütüphaneleri
import java.util.Date;
import java.util.List;
import java.util.Set;

// Spring Security UserDetails (validateToken metodu için)
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ✅ Token üret
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // roller claim olarak eklenir
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public long getExpirationMillis(String token){
        Date expirationDate = getClaims(token).getExpiration();
        long now = System.currentTimeMillis();
        long diff = expirationDate.getTime() - now;
        return diff > 0 ? diff : 0;
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
        try {
            return Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (ClaimJwtException e){
            return e.getClaims();
        }
}
}