package com.example.memorip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "travels")
public final class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "plan_id",nullable = false)
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false)
    private int likes;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_public",nullable = false)
    private Boolean isPublic;
}
