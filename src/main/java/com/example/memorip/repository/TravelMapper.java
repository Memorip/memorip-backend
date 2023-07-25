package com.example.memorip.repository;

import com.example.memorip.dto.TravelDTO;
import com.example.memorip.entity.Travel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TravelMapper {
    TravelMapper INSTANCE = Mappers.getMapper(TravelMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "plan.id", target = "planId")
    TravelDTO travelToTravelDTO(Travel travel);

    Travel travelDTOtoTravel(TravelDTO dto);
}
