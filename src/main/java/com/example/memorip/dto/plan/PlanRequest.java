package com.example.memorip.dto.plan;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
public class PlanRequest {
    private int userId;
    private List<String> city;
    private Date startDate;
    private Date endDate;
    private String tripType;
    private List<Integer> participants;
    private Boolean isPublic;

    public PlanDTO toDto(PlanRequest request) {
        return PlanDTO.builder()
                .id(0)
                .userId(request.getUserId())
                .city(request.getCity())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .tripType(request.getTripType())
                .participants(request.getParticipants())
                .createdAt(LocalDateTime.now())
                .isPublic(request.getIsPublic())
                .views(0)
                .likes(0)
                .build();
    }
}