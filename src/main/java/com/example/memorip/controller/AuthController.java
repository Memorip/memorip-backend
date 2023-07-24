package com.example.memorip.controller;

import com.example.memorip.dto.JwtResponseDTO;
import com.example.memorip.dto.LoginRequestDTO;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@Tag(name = "auth", description = "auth api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api")
@Validated
public class AuthController {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${jwt.expiration}")
    private int expiration;


    public AuthController(JwtTokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Operation(summary = "로그인", description = "로그인 메서드입니다.")
    @PostMapping("/login")
    public ResponseEntity<DefaultRes<JwtResponseDTO>> authorize(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "로그인 요청 객체", required = true, content = @Content(schema = @Schema(implementation = LoginRequestDTO.class)))
            @Valid @RequestBody LoginRequestDTO loginDto, HttpServletResponse response
    ) {
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication);

            log.info("jwt: {}", jwt);

            Cookie cookie = new Cookie("accessToken", jwt);
            cookie.setMaxAge(expiration);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setDomain("memorip.vercel.app");
            cookie.setDomain("localhost");
            response.addCookie(cookie);

            return new ResponseEntity<>(
                    DefaultRes.res(200, "로그인 성공", new JwtResponseDTO(jwt)), HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃 메서드입니다.")
    @PostMapping("/logout")
    public ResponseEntity<DefaultRes<Void>> logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("accessToken")){
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        return new ResponseEntity<>(DefaultRes.res(200, "로그아웃 성공"), HttpStatus.OK);
    }
}
