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
    private String thumbnail;
}