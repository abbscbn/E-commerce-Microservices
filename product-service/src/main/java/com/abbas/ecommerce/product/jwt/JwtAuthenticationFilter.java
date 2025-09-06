package com.abbas.ecommerce.product.jwt;
import com.abbas.ecommerce.common.exception.AuthBaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.product.services.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new AuthBaseException(new ErrorMessage(ErrorMessageType.TOKEN_IS_NOT_VALID, null));
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                throw new AuthBaseException(new ErrorMessage(ErrorMessageType.TOKEN_EXPIRED, token));
            }

            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                throw new AuthBaseException(new ErrorMessage(ErrorMessageType.TOKEN_BLACKLIST, token));
            }

            // Token geçerli → authentication oluştur
            String username = jwtUtil.extractUsername(token);
            var roles = jwtUtil.extractRoles(token).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, roles);

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (AuthBaseException e) {
            SecurityContextHolder.clearContext();
            // EntryPoint'i direkt çağırıyoruz
            jwtAuthenticationEntryPoint.commence(request, response, e);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            jwtAuthenticationEntryPoint.commence(request, response,
                    new AuthBaseException(new ErrorMessage(ErrorMessageType.TOKEN_IS_NOT_VALID, authHeader)));
        }
    }
}
