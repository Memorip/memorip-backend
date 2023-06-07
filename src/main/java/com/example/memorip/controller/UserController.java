package com.example.memorip.controller;

import com.example.memorip.dto.SignUpDTO;
import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.User;
import com.example.memorip.repository.UserMapper;
import com.example.memorip.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "user", description = "유저 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/checkEmail")
    public ResponseEntity checkEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.isEmailTaken(email));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpDTO> signup(
            @Valid @RequestBody SignUpDTO signUpDTO
    ) {
        return ResponseEntity.ok(userService.signup(signUpDTO));
    }

    // 현재 SecurityContext에 저장된 username 유저 정보를 가져옴
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserDTO> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    // username(email)을 기준으로 유저 정보를 가져옴
    @GetMapping("/users/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(email));
    }
  
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @DeleteMapping ("/users/{email}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.deactivateUser(email));
    }
}

