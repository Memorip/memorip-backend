package com.example.memorip.dto.plan;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PlanResponse {
    private int id;
    private int userId;
    private String nickname;
    private List<String> city;
    private Date startDate;
    private Date endDate;
    private String tripType;
    private List<Integer> participants;
    private LocalDateTime createdAt;
    private Boolean isPublic;
    private int views;
    private int likes;

    public static PlanResponse convertToResponseDto(PlanDTO dto, String nickname) {
        PlanResponse resDto = new PlanResponse();
        resDto.setId(dto.getId());
        resDto.setUserId(dto.getUserId());
        resDto.setNickname(nickname);
        resDto.setCity(dto.getCity());
        resDto.setStartDate(dto.getStartDate());
        resDto.setEndDate(dto.getEndDate());
        resDto.setTripType(dto.getTripType());
        resDto.setParticipants(dto.getParticipants());
        resDto.setCreatedAt(dto.getCreatedAt());
        resDto.setIsPublic(dto.getIsPublic());
        resDto.setViews(dto.getViews());
        resDto.setLikes(dto.getLikes());
        return resDto;
    }
}