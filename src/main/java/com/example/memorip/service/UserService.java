package com.example.memorip.service;

import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.Users;
import com.example.memorip.repository.UserMapper;
import com.example.memorip.repository.UserMapperConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserDTO> getUsers() {
        List<Users> usersList = userMapper.getUsers();
        return UserMapperConverter.INSTANCE.usersToUserDTOList(usersList);
//        return usersList.stream()
//                .map(UserMapperConverter.INSTANCE::usersToUserDTO)
//                .collect(Collectors.toList());
    }

    // 기타 서비스 메서드
}
