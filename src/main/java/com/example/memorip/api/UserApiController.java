package com.example.memorip.api;

import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.Users;
import com.example.memorip.repository.UserMapper;
import com.example.memorip.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserMapper userMapper;
    public UserApiController(UserService userService, UserMapper userMapper){
        this.userService = userService;
        this.userMapper=userMapper;
    }

    @GetMapping("api/users")
    public List<Users> index(){
        return userService.index();
    }

    @GetMapping("api/userlist")
    public List<UserDTO> getusers(){
        //List<UserDTO> lists = userMapper.getUsers();
        return userMapper.getUsers();
    }
}

