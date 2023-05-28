package com.example.memorip.repository;

import com.example.memorip.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<Users> getUsers();
}