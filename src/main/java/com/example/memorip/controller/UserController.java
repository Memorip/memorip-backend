package com.example.memorip.controller;

import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.Users;
import com.example.memorip.repository.UserMapper;
import com.example.memorip.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

//    private final UserService userService;
    private final UserMapper userMapper;
    public UserController(UserService userService, UserMapper userMapper){
//        this.userService = userService;
        this.userMapper=userMapper;
    }

    @GetMapping("/userlist")
    public List<UserDTO> getusers(){
//        List<UserDTO> lists = userMapper.getUsers();
        return userMapper.getUsers();
    }
}

