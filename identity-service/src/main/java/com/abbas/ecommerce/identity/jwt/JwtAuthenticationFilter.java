package com.abbas.ecommerce.identity.jwt;

import com.abbas.ecommerce.common.exception.AuthBaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.identity.services.TokenBlacklistService;
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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // JWT filtre sadece /auth/logout veya başka korunan endpointlerde çalışsın
        return path.equals("/auth/login") || path.equals("/auth/register");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;



        try{


            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                SecurityContextHolder.clearContext();
                throw new AuthBaseException(
                        new ErrorMessage(ErrorMessageType.TOKEN_IS_NOT_VALID, token)
                );
            }

            token = authHeader.substring(7);

            // Doğrudan exception fırlat, zinciri bitir
            if (!jwtUtil.validateToken(token)) {
                SecurityContextHolder.clearContext();
                throw new AuthBaseException(
                        new ErrorMessage(ErrorMessageType.TOKEN_EXPIRED, token)
                );
            }

            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                SecurityContextHolder.clearContext();
                throw new AuthBaseException(
                        new ErrorMessage(ErrorMessageType.TOKEN_BLACKLIST, token)
                );
            }



            // Token geçerli ise authentication nesnesi oluştur
            String username = jwtUtil.extractUsername(token);
            var roles = jwtUtil.extractRoles(token).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, roles);

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        }

        catch (AuthBaseException e){

            if(e.getErrorMessage().getErrorMessageType().getCode()==1007){
                throw new AuthBaseException(new ErrorMessage(ErrorMessageType.TOKEN_EXPIRED,authHeader));
            }
            else if(e.getErrorMessage().getErrorMessageType().getCode()==1008){

                throw new AuthBaseException(new ErrorMessage(ErrorMessageType.TOKEN_BLACKLIST,authHeader));
            }
            else {
                throw new AuthBaseException(new ErrorMessage(ErrorMessageType.TOKEN_IS_NOT_VALID,authHeader));
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new AuthBaseException(new ErrorMessage(ErrorMessageType.TOKEN_IS_NOT_VALID,authHeader));
        }
    }

}