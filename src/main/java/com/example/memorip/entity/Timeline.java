package com.example.memorip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "timelines")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimelineType type;

    @Column(nullable = false)
    private Date date;

    @Column
    private String memo;

    @Column
    private String data;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

//    @ManyToMany
//    @JoinTable(
//        name = "timeline_file",
//        joinColumns = {@JoinColumn(name = "timeline_id", referencedColumnName = "id")},
//        inverseJoinColumns = {@JoinColumn(name = "file_id", referencedColumnName = "id")}
//    )
//    private Set<File> files;
}