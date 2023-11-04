package com.example.memorip.service;

import com.example.memorip.dto.user.SignUpDTO;
import com.example.memorip.dto.user.UpdatePasswordDTO;
import com.example.memorip.dto.user.UpdateUserDTO;
import com.example.memorip.entity.Authority;
import com.example.memorip.entity.User;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.repository.UserRepository;
import com.example.memorip.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.memorip.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisUtil = redisUtil;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_USER));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email).orElse(null) != null;
    }

    @Transactional(readOnly = true)
    public boolean isNicknameTaken(String nickname) {
        return userRepository.findOneByNickname(nickname).orElse(null) != null;
    }

    @Transactional
    public User signup(SignUpDTO signUpDTO){
        String email = signUpDTO.getEmail();
        if(isEmailTaken(email)){
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 사용중인 이메일입니다.");
        }

        if(isNicknameTaken(signUpDTO.getNickname())){
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 사용중인 닉네임입니다.");
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

        if(redisUtil.existData(email)){
            redisUtil.deleteData(email);
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserInfo(UpdateUserDTO updateUserDTO){
        User user = getUserWithAuthorities(SecurityUtil.getCurrentUsername().orElse(null));

        if(updateUserDTO.getNickname()!=null){
            if(isNicknameTaken(updateUserDTO.getNickname())){
                throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 사용중인 닉네임입니다.");
            }
            user.setNickname(updateUserDTO.getNickname());
        }
        if(updateUserDTO.getProfile()!=null) {
            user.setProfile(updateUserDTO.getProfile());
        }

        return userRepository.save(user);
    }

    @Transactional
    public User updatePassword(UpdatePasswordDTO updatePasswordDTO){
        User user = getUserWithAuthorities(SecurityUtil.getCurrentUsername().orElse(null));

        if(!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER_PASSWORD);
        }

        if(!Objects.equals(updatePasswordDTO.getNewPassword(), updatePasswordDTO.getNewPasswordConfirm())){
            throw new CustomException(ErrorCode.PASSWORD_CONFIRM_NOT_MATCHED);
        }

        if(passwordEncoder.matches(updatePasswordDTO.getNewPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_CONFIRM_NOT_MATCHED, "기존 비밀번호와 동일합니다.");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public User deactivateUser(String email){
        User user = userRepository.findOneWithAuthoritiesByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setEmail(user.getEmail() + "_deactivated_" + new Date().getTime());
        user.setIsActive(false);

        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(int id){
        return userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public boolean getParticipantById(List<Integer> list){
        for (Integer participant : list) {
            userRepository.findById(participant).orElseThrow(() -> new CustomException(ErrorCode.PARTICIPANT_NOT_FOUND));
        }
        return true;
    }
}
