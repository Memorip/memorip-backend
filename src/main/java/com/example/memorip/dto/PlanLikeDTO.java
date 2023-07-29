package com.example.memorip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanLikeDTO {
    private int id;
    private int userId;
    private int planId;
    private int isLiked;
}
