package com.example.memorip.repository;

import com.example.memorip.dto.user.UserDTO;
import com.example.memorip.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<UserDTO> usersToUserDTOs(List<User> users);

    @Mappings({
        @Mapping(target = "myPlanCount", expression = "java(mapUserToMyPlanCount(user))"),
        @Mapping(target = "myTravelCount", expression = "java(mapUserToMyTravelCount(user))")
    })
    UserDTO userToUserDTO(User user);


    default int mapUserToMyPlanCount(User user) {
        return user.getPlans().size();
    }

    default int mapUserToMyTravelCount(User user) {
        return user.getTravels().size();
    }

}
