package com.example.memorip.jwt;

import com.example.memorip.exception.DefaultRes;
import com.example.memorip.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
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