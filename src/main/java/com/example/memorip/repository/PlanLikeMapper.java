package com.example.memorip.repository;

import com.example.memorip.dto.PlanLikeDTO;
import com.example.memorip.entity.PlanLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlanLikeMapper {
    PlanLikeMapper INSTANCE = Mappers.getMapper(PlanLikeMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "plan.id", target = "planId")
    PlanLikeDTO likeToLikeDTO(PlanLike like);

    PlanLike LikeDTOtoLike(PlanLikeDTO dto);
}
