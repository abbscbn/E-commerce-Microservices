package com.abbas.ecommerce.order.jwt;

import com.abbas.ecommerce.common.exception.AuthBaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.common.response.RootResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        WebRequest webRequest = new ServletWebRequest(request);

        String errorMsg;

        if (authException instanceof AuthBaseException abe) {
            ErrorMessage errorMessage = abe.getErrorMessage();
            errorMsg = errorMessage != null ? errorMessage.preparedErrorMessage() : abe.getMessage();

        } else {
            errorMsg = new ErrorMessage(ErrorMessageType.TOKEN_IS_NOT_VALID, "").preparedErrorMessage();

        }

        RootResponse<String> error = RootResponse.error(errorMsg, webRequest);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
