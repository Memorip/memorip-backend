package com.example.memorip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDTO {
    private int id;
    private int userId;
    private int planId;
    private int isLiked;
}
