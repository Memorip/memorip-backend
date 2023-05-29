package com.example.memorip.repository;

import com.example.memorip.dto.PlanDTO;

import com.example.memorip.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);

    @Mapping(source = "user_id", target = "userId")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "start_date", target = "startDate")
    @Mapping(source = "end_date", target = "endDate")
    @Mapping(source = "trip_type", target = "tripType")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "created_at", target = "createdAt")
    PlanDTO planToPlanDTO(Plan plan);
}
