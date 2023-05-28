package com.example.memorip.service;

import com.example.memorip.dto.UserDTO;
import com.example.memorip.repository.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserDTO> getUsers() {
        return userMapper.getUsers();
    }

}
