package com.example.memorip.dto.plan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanParticipantRequest {
    private int userId;
    private int planId;
}
