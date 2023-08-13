package com.example.memorip.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private String city;

    @Column(name = "start_date",nullable = false)
    private Date startDate;

    @Column(name="end_date",nullable = false)
    private Date endDate;

    @Column(name = "trip_type")
    private String tripType;

    @Column
    private String participants;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_public",nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false)
    private int likes;
}