package com.example.memorip.repository;

import com.example.memorip.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.util.List;

@Mapper
public interface UserMapper {
//    @Select("SELECT * FROM users")
    @Results({
            @Result(property = "createdAt", column = "created_at")
    })
    List<UserDTO> getUsers();
}
