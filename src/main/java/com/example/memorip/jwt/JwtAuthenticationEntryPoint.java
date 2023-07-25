package com.example.memorip.jwt;

import com.example.memorip.exception.DefaultRes;
import com.example.memorip.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        ErrorCode errorCode = ErrorCode.INVALID_AUTH_TOKEN;
        ResponseEntity<DefaultRes<String>> responseEntity = ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(DefaultRes.res(
                        errorCode.getHttpStatus().value(),
                        errorCode.getDetail()
                ));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseEntity.getStatusCode().value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
    }
}