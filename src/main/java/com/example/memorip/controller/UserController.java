package com.example.memorip.controller;

import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.Users;
import com.example.memorip.repository.UserMapper;
import com.example.memorip.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }


//    private final UserService userService;
//    private final UserMapper userMapper;
//    public UserController(UserService userService, UserMapper userMapper){
////        this.userService = userService;
//        this.userMapper=userMapper;
//    }

//    @GetMapping("/user/{id}")
//    public void getUser(@PathVariable Integer id){
//        log.info("id: {}", id);
//        Users user = UserMapper.INSTANCE.getUser(id);
//        log.info("user: {}", user);
//        UserDTO dto = UserMapper.INSTANCE.userToUserDTO(user);
//        log.info("dto: {}", dto);
//    }

//    @GetMapping("/userlist")
//    public List<Users> getusers(){
////        List<UserDTO> lists = userMapper.getUsers();
//        return userMapper.getUsers();
//    }
}

