package com.example.memorip.repository;

import com.example.memorip.dto.SignUpDTO;
import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<UserDTO> usersToUserDTOs(List<User> users);

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

}
