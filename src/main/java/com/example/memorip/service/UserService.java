package com.example.memorip.service;

import com.example.memorip.entity.Users;
import com.example.memorip.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> index(){
        return userRepository.findAll();
    }
}
