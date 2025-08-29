package com.abbas.ecommerce.product.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
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
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        filterChain.doFilter(request, response);

        }

    }

