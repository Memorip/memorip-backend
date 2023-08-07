package com.example.memorip.dto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
public class PlanDTO {
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
}