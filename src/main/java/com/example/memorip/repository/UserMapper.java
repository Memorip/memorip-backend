package com.example.memorip.repository;

import com.example.memorip.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDTO> getUsers();
}
