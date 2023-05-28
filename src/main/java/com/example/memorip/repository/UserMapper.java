package com.example.memorip.repository;

import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

//    @Select("SELECT * FROM users WHERE id = #{id}")
//    UserDTO getUser(@Param("id") int id);

    UserDTO userToUserDTO(Users user);

//    Users userDTOToUser(UserDTO userDTO);

//    List<Users> getUsers();

    Users getUser(Integer id);
}