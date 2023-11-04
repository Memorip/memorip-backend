package com.example.memorip.repository;

import com.example.memorip.dto.PlanLikeDTO;
import com.example.memorip.dto.plan.PlanDTO;
import com.example.memorip.entity.Plan;
import com.example.memorip.entity.PlanLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PlanLikeMapper {
    PlanLikeMapper INSTANCE = Mappers.getMapper(PlanLikeMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "plan.id", target = "planId")
    // plan -> planDTO??
    @Mapping(source = "plan", target = "planDTO", qualifiedByName = "mapPlanToPlanDTO")
    PlanLikeDTO likeToLikeDTO(PlanLike like);

    PlanLike LikeDTOtoLike(PlanLikeDTO dto);

    @Named("mapPlanToPlanDTO")
    static PlanDTO mapPlanToPlanDTO(Plan plan) {
        if (plan == null) {
            return null;
        }

        PlanDTO planDTO = PlanDTO.builder()
                .id(plan.getId())
                .userId(plan.getUser().getId())
                .city(Arrays.asList(plan.getCity().split(",")))
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .tripType(plan.getTripType())
                .participants(Arrays.stream(plan.getParticipants().split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .createdAt(plan.getCreatedAt())
                .isPublic(plan.getIsPublic())
                .views(plan.getViews())
                .likes(plan.getLikes())
                .thumbnail(plan.getThumbnail())
                .build();

        return planDTO;
    }
}
