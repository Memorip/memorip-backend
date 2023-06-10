package com.example.memorip.service;

import com.example.memorip.dto.SignUpDTO;
import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.Authority;
import com.example.memorip.entity.User;
import com.example.memorip.exception.NotFoundUserException;
import com.example.memorip.repository.UserMapper;
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
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email).orElse(null) != null;
    }

    @Transactional
    public SignUpDTO signup(SignUpDTO signUpDTO){
        if(userRepository.findOneWithAuthoritiesByEmail(signUpDTO.getEmail()).orElse(null) != null){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
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

        return SignUpDTO.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDTO getUserWithAuthorities(String email) {
        return userMapper.userToUserDTO(userRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDTO getMyUserWithAuthorities() {
        return userMapper.userToUserDTO(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundUserException("User not found"))
        );
    }

    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.usersToUserDTOs(users);
    }

    @Transactional
    public UserDTO deactivateUser(String email){
        User user = userRepository.findOneWithAuthoritiesByEmail(email).orElse(null);
        if(user == null){
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }

        user.setEmail(user.getEmail() + "_deactivated_" + new Date().getTime());
        user.setIsActive(false);

        return userMapper.userToUserDTO(userRepository.save(user));
    }

}
