package com.abbas.ecommerce.identity.jwt;

// Spring Framework & Spring Security
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.identity.services.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
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


        String username = null;
        String token= null;

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if(!jwtUtil.validateToken(token)){
                logger.warn("JWT SÜRESİ DOLMUŞTUR");

                request.setAttribute("TOKENEXPIRED", new ErrorMessage(ErrorMessageType.TOKEN_EXPIRED, token));

            }

            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                logger.warn("JWT blacklisted!");

                request.setAttribute("BLACKLIST", new ErrorMessage(ErrorMessageType.TOKEN_BLACKLIST, token));
                filterChain.doFilter(request, response);
                return;
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
        }

        catch (ExpiredJwtException e){
            logger.warn("Token süresi dolmuştur "+e.getMessage());

        }
        catch (Exception e){
            logger.warn("Genel bir hata oluştu "+e.getMessage());
        }

        filterChain.doFilter(request, response);

    }

}
