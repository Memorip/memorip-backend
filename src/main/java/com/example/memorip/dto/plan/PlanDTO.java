package com.example.memorip.dto.plan;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PlanDTO {
    private int id;
    private int userId;
    private List<String> city;
    private Date startDate;
    private Date endDate;
    private String tripType;
    private List<Integer> participants;
    private LocalDateTime createdAt;
    private Boolean isPublic;
    private int views;
    private int likes;

    @Builder
    public PlanDTO(int id,int userId,List<String> city, Date startDate, Date endDate,
                   String tripType, List<Integer> participants, LocalDateTime createdAt,
                   Boolean isPublic, int views,int likes) {
        this.id = id;
        this.userId = userId;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripType = tripType;
        this.participants = participants;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.views = views;
        this.likes = likes;
    }
}