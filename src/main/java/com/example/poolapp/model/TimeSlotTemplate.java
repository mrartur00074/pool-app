package com.example.poolapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Entity
@Table(name = "time_slots_template")
@Getter
@Setter
public class TimeSlotTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_template_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private DayTemplate template;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Column(name = "max_visitors", nullable = false)
    private Integer maxVisitors = 10;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;
}