package com.example.memorip.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
public class PlanDTO {
    private int id;
    private int userId;

    // @NotEmpty(message = "not empty")
    private List<String> city;
    private Date startDate;
    private Date endDate;
    // @NotBlank(message = "어이없네 진짜 자바")
    private String tripType;
    private List<Integer> participants;
    private Date createdAt;
    private Boolean isPublic;


}


