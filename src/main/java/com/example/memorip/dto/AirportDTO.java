package com.example.memorip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirportDTO {
    private int id;
    private String koreanName; // 김포 국제공항
    private String englishName; // Gimpo International Airport
    private String code; // GMP
    private String city; // 서울
}
