package com.example.memorip.service;

import com.example.memorip.dto.SignUpDTO;
import com.example.memorip.entity.Authority;
import com.example.memorip.entity.User;
import com.example.memorip.exception.NotFoundException;
import com.example.memorip.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.memorip.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email).orElse(null) != null;
    }

    @Transactional
    public User signup(SignUpDTO signUpDTO){
        if(userRepository.findOneWithAuthoritiesByEmail(signUpDTO.getEmail()).orElse(null) != null){
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .nickname(signUpDTO.getNickname())
                .authorities(Collections.singleton(authority))
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found:"+email));
    }

    @Transactional(readOnly = true)
    public User getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundException("User not found"));

    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User deactivateUser(String email){
        User user = userRepository.findOneWithAuthoritiesByEmail(email)
                .orElseThrow(()-> new NotFoundException("User not found:"+email));

        user.setEmail(user.getEmail() + "_deactivated_" + new Date().getTime());
        user.setIsActive(false);

        return userRepository.save(user);
    }

}
