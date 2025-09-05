package com.abbas.ecommerce.order.jwt;

import com.abbas.ecommerce.common.exception.AuthBaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.common.response.RootResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        WebRequest webRequest = new ServletWebRequest(request);

        // Eğer AuthBaseException geldiyse detaylı hata döndür
        if (authException instanceof AuthBaseException abe) {
            ErrorMessage errorMessage = abe.getErrorMessage();
            RootResponse<String> error = RootResponse.error(
                    errorMessage != null ? errorMessage.preparedErrorMessage() : abe.getMessage(),
                    webRequest
            );
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return;
        }

        // Fallback: Diğer hata tipleri için genel hata mesajı
        ErrorMessage errorMessage = new ErrorMessage(ErrorMessageType.TOKEN_IS_NOT_VALID, "");
        RootResponse<String> error = RootResponse.error(errorMessage.preparedErrorMessage(), webRequest);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
