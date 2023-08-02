package com.example.memorip.controller;

import com.example.memorip.dto.user.JwtResponseDTO;
import com.example.memorip.dto.user.LoginRequestDTO;
import com.example.memorip.dto.user.SignUpDTO;
import com.example.memorip.dto.user.UserDTO;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.jwt.JwtTokenProvider;
import com.example.memorip.repository.UserMapper;
import com.example.memorip.service.UserService;
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
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@Tag(name = "auth", description = "인증 관련 api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api")
@Validated
public class AuthController {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    @Value("${jwt.expiration}")
    private int expiration;


    public AuthController(JwtTokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    @Operation(summary = "이메일 중복검사", description = "이미 사용중인 이메일이라면 true, 사용가능한 이메일은 false를 return 합니다.")
    @GetMapping("/checkEmail")
    public ResponseEntity<DefaultRes<Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean isEmailTaken = userService.isEmailTaken(email);
        return isEmailTaken?
                new ResponseEntity<>(DefaultRes.res(409, "이미 가입된 이메일입니다", true), HttpStatus.CONFLICT) :
                new ResponseEntity<>(DefaultRes.res(200, "사용 가능한 이메일입니다", false), HttpStatus.OK);
    }

    @Operation(summary = "닉네임 중복검사", description = "이미 사용중인 닉네임이라면 true, 사용가능한 닉네임은 false를 return 합니다.")
    @GetMapping("/checkNickname")
    public ResponseEntity<DefaultRes<Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        boolean isNicknameTaken = userService.isNicknameTaken(nickname);
        return isNicknameTaken?
                new ResponseEntity<>(DefaultRes.res(409, "이미 사용중인 닉네임입니다", true), HttpStatus.CONFLICT) :
                new ResponseEntity<>(DefaultRes.res(200, "사용 가능한 닉네임입니다", false), HttpStatus.OK);
    }

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/signup")
    public ResponseEntity<DefaultRes<UserDTO>> signup(
            @Valid @RequestBody SignUpDTO signUpDTO
    ) {
        UserDTO user = UserMapper.INSTANCE.userToUserDTO(userService.signup(signUpDTO));
        return new ResponseEntity<>(DefaultRes.res(201, "회원가입 성공", user), HttpStatus.CREATED);
    }


    @Operation(summary = "로그인", description = "요청 성공 시, 토큰을 발급하여 쿠키에 담아 전송합니다.")
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

            ResponseCookie cookie = ResponseCookie.from("accessToken", jwt)
                    .maxAge(expiration)
                    .path("/")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            return new ResponseEntity<>(
                    DefaultRes.res(200, "로그인 성공", new JwtResponseDTO(jwt)), HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    @Operation(summary = "로그아웃", description = "요청 성공시, 토큰을 쿠키에서 삭제합니다.")
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
