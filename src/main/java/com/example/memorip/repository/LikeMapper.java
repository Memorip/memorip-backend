package com.example.memorip.repository;

import com.example.memorip.dto.LikeDTO;
import com.example.memorip.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    LikeMapper INSTANCE = Mappers.getMapper(LikeMapper.class);


    @Mapping(source = "user_id", target = "userId")
    @Mapping(source = "plan_id", target = "planId")
    @Mapping(source = "is_liked",target = "isLiked")
    LikeDTO likeToLikeDTO(Like like);

    @Mapping(source = "userId",target = "user_id")
    @Mapping(source = "planId", target = "plan_id")
    @Mapping(source = "isLiked",target = "is_liked")
    Like LikeDTOtoLike(LikeDTO dto);


}
