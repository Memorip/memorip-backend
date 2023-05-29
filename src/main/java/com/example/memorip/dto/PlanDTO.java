package com.example.memorip.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
public class PlanDTO {
    private int id;
    private int userId;
    private List<String> city;
    private Date startDate;
    private Date endDate;
    private String tripType;
    private List<Integer> participants;
    private Date createdAt;
    private boolean isPublic;
}
