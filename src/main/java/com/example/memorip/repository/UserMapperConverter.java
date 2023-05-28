package com.example.memorip.repository;

import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapperConverter {
    UserMapperConverter INSTANCE = Mappers.getMapper(UserMapperConverter.class);

    @Mapping(source = "created_at", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    UserDTO usersToUserDTO(Users users);

    List<UserDTO> usersToUserDTOList(List<Users> usersList);
}
