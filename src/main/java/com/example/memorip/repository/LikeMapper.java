package com.example.memorip.repository;

import com.example.memorip.dto.LikeDTO;
import com.example.memorip.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    LikeMapper INSTANCE = Mappers.getMapper(LikeMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "plan.id", target = "planId")
    LikeDTO likeToLikeDTO(Like like);

    Like LikeDTOtoLike(LikeDTO dto);
}
