package com.example.memorip.controller;

import com.example.memorip.dto.SignUpDTO;
import com.example.memorip.dto.UserDTO;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.service.UserService;
import com.example.memorip.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "user api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "이메일 중복검사", description = "이미 있는 이메일이라면 true, 아직 없다면 false를 return 합니다.")
    @GetMapping("/checkEmail")
    public ResponseEntity<DefaultRes<Boolean>> checkEmail(@RequestParam("email") String email) {
        return  userService.isEmailTaken(email)?
                new ResponseEntity<>(DefaultRes.res(409, "이미 가입된 이메일입니다", userService.isEmailTaken(email)), HttpStatus.CONFLICT) :
                new ResponseEntity<>(DefaultRes.res(200, "사용 가능한 이메일입니다", userService.isEmailTaken(email)), HttpStatus.OK);
    }

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/signup")
    public ResponseEntity<DefaultRes<SignUpDTO>> signup(
            @Valid @RequestBody SignUpDTO signUpDTO
    ) {
        return new ResponseEntity<>(DefaultRes.res(201, "회원가입 성공", userService.signup(signUpDTO)), HttpStatus.CREATED);
    }

    @Operation(summary = "본인데이터 조회", description = "현재 SecurityContext에 저장된 username 유저 정보를 가져옵니다.")
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<DefaultRes<UserDTO>> getMyUserInfo() {
        return new ResponseEntity<>(DefaultRes.res(200, "본인 정보 조회 성공", userService.getMyUserWithAuthorities()), HttpStatus.OK);
    }

    @Operation(summary = "[admin]유저데이터 조회", description = "username(email)을 기준으로 유저 정보를 가져옵니다. ADMIN 권한이 필요합니다.")
    @GetMapping("/users/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DefaultRes<UserDTO>> getUserInfo(@PathVariable String email) {
        return new ResponseEntity<>(DefaultRes.res(200, "유저 정보 조회 성공", userService.getUserWithAuthorities(email)), HttpStatus.OK);
    }

    @Operation(summary = "[admin]유저목록 조회", description = "유저 전체 목록을 가져옵니다. ADMIN 권한이 필요합니다.")
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DefaultRes<List<UserDTO>>> getUsers() {
        return new ResponseEntity<>(DefaultRes.res(200, "유저 목록 조회 성공", userService.getUsers()), HttpStatus.OK);
    }

    // TODO: 로그아웃

    @Operation(summary = "탈퇴", description = "현재 SecurityContext에 저장된 username 기반으로 유저를 삭제합니다.")
    @DeleteMapping ("/user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<DefaultRes<UserDTO>> deleteUser() {
        String email = SecurityUtil.getCurrentUsername().orElse(null);
        return new ResponseEntity<>(DefaultRes.res(200, "탈퇴 성공", userService.deactivateUser(email)), HttpStatus.OK);
    }

    @Operation(summary = "[admin]탈퇴", description = "username(email)로 유저를 삭제합니다. ADMIN 권한이 필요합니다.")
    @DeleteMapping ("/users/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DefaultRes<UserDTO>> deleteUser(@PathVariable String email) {
        return new ResponseEntity<>(DefaultRes.res(200, "탈퇴 성공", userService.deactivateUser(email)), HttpStatus.OK);
    }
}

