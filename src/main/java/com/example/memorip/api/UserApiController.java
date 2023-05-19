package com.example.memorip.api;

import com.example.memorip.entity.Users;
import com.example.memorip.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserApiController {
    @Autowired
    private UserService userService;

    @GetMapping("api/users")
    public List<Users> index(){
        return userService.index();
    }
}
