package com.example.memorip.controller;

import com.example.memorip.entity.Users;
import com.example.memorip.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j // 로깅 어노테이션
public class UserController {
    @Autowired // 스프링부트가 미리 생성해놓은 객체를 가져다 자동 연결!
    private UserRepository userRepository;

    @GetMapping("/users")
    public String getUsers(){
        // 1. 모든 User를 가져온다.
        List<Users> userEntityList = userRepository.findAll();

        //
        for (Users user:userEntityList){
            log.info(user.toString());
        }



        // 3. 뷰 페이지를 설정
        return "";
    }
}
