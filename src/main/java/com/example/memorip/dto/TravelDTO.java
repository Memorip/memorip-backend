package com.example.memorip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TravelDTO {
    private int id;
    private int planId;
    private int userId;
    private String title;
    private String content;
    private int views;
    private int likes;
    private LocalDateTime createdAt;
    private boolean isPublic;
}
