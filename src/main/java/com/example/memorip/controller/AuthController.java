package com.example.memorip.controller;

import com.example.memorip.dto.JwtResponseDTO;
import com.example.memorip.dto.LoginRequestDTO;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.jwt.JwtAuthenticationFilter;
import com.example.memorip.jwt.JwtTokenProvider;
import com.example.memorip.util.Error;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@Tag(name = "auth", description = "auth api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(JwtTokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Operation(summary = "로그인", description = "로그인 메서드입니다.")
    @PostMapping("/login")
    public ResponseEntity<?> authorize(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "로그인 요청 객체", required = true, content = @Content(schema = @Schema(implementation = LoginRequestDTO.class)))
            @Valid @RequestBody LoginRequestDTO loginDto) {
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return new ResponseEntity<>(new JwtResponseDTO(jwt), httpHeaders, HttpStatus.OK);
        }catch (Exception e){
            Error error = new Error(400, "잘못된 이메일 또는 패스워드를 입력했어요");
            return new ResponseEntity<>(DefaultRes.res(error.getStatus(), error.getMessage(), ""), HttpStatus.BAD_REQUEST);
        }
    }
}
