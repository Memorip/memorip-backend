package com.example.memorip.repository;

import com.example.memorip.dto.TravelLikeDTO;
import com.example.memorip.entity.TravelLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TravelLikeMapper {
    TravelLikeMapper INSTANCE = Mappers.getMapper(TravelLikeMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "travel.id", target = "travelId")
    TravelLikeDTO likeToLikeDTO(TravelLike like);

    TravelLike LikeDTOtoLike(TravelLikeDTO dto);
}
