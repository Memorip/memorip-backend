package com.example.memorip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelLikeDTO {
    private int id;
    private int userId;
    private int travelId;
    private int isLiked;
}
