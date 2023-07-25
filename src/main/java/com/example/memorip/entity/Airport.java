package com.example.memorip.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airports")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="korean_name")
    private String koreanName; // 김포 국제공항

    @Column(name = "english_name")
    private String englishName; // Gimpo International Airport

    @Column
    private String code; // GMP

    @Column
    private String city; // 서울
}
