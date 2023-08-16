package com.example.memorip.dto.plan;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Getter
@Setter
public class PlanRequest {
    private int userId;
    private List<String> city;
    private Date startDate;
    private Date endDate;
    private String tripType;
    private List<Integer> participants;
    private Boolean isPublic;
}