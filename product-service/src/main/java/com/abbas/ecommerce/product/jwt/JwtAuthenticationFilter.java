package com.abbas.ecommerce.product.jwt;

import com.abbas.ecommerce.product.services.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String username = null;
        String token= null;


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
             token=authHeader.substring(7);

            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                logger.error("JWT geçersiz: " + e.getMessage());
                throw new RuntimeException("JWT geçersiz:"); //BURADAKİ HATALRI YAKALA AŞAĞISI İÇİNDE HATA YAKALA
            }
        }


            if (jwtUtil.validateToken(token)) {
                 username = jwtUtil.extractUsername(token);
                var roles = jwtUtil.extractRoles(token).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, roles);

                if (tokenBlacklistService.isTokenBlacklisted(token)) {
                    logger.warn("JWT blacklisted!");
                    filterChain.doFilter(request, response);
                    return;
                }

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        filterChain.doFilter(request, response);

        }

    }

