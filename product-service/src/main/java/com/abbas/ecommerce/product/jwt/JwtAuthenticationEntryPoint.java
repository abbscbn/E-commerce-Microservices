package com.abbas.ecommerce.product.jwt;

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
     ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        WebRequest webRequest = new ServletWebRequest(request);

        // 1) Filtrede bıraktığın zengin hata bilgisi varsa onu kullan
        Object blacklist = request.getAttribute("BLACKLIST");
        Object expired = request.getAttribute("TOKENEXPIRED");

        if(expired instanceof ErrorMessage em){
            RootResponse<String> error = RootResponse.error(em.preparedErrorMessage(), webRequest);
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return;
        }

        if (blacklist instanceof ErrorMessage em) {
            RootResponse<String> error = RootResponse.error(em.preparedErrorMessage(), webRequest);
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return;
        }

        // 2) Doğrudan senin exception’ın geldiyse kullan
        if (authException instanceof AuthBaseException abe) {
            RootResponse<String> error = RootResponse.error(abe.getMessage(), webRequest);
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return;
        }

        // 3) Fallback: default mesaj
        ErrorMessage errorMessage= new ErrorMessage(ErrorMessageType.TOKEN_IS_NOT_VALID,"");
        RootResponse<String> error = RootResponse.error(errorMessage.preparedErrorMessage(), webRequest);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

}
