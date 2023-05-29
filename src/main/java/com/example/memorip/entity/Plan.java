package com.example.memorip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "plans")
public final class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private int user_id;

    @Column
    private String city;

    @Column
    private Date start_date;

    @Column
    private Date end_date;

    @Column
    private String trip_type;

    @Column
    private String participants;

    @Column
    private Date created_at;

    @Column
    private boolean isPublic;
}
