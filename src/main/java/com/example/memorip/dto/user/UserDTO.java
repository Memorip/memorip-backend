package com.example.memorip.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String email;
    private String nickname;
    private String profile;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("is_active")
    private Boolean isActive;
    private int myPlanCount;
    private int myTravelCount;
}