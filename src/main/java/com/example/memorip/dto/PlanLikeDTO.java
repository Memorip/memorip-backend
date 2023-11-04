package com.example.memorip.dto;

import com.example.memorip.dto.plan.PlanDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanLikeDTO {
    private int id;
    private int userId;
    private int planId;
    private PlanDTO planDTO;
    private int isLiked;

}
