package com.example.memorip.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
public class ReqPlanDTO {
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
}