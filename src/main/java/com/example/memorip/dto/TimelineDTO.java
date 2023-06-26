package com.example.memorip.dto;

import com.example.memorip.entity.TimelineType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class TimelineDTO {
    private int id;
    private TimelineType type;
    private Date date;
    private String memo;
    private String data;
    private LocalDateTime createdAt;
    private int planId;
}
