package com.example.poolapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "calendar")
@Getter
@Setter
public class Calendar {
    @Id
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private DayTemplate template;

    @Column(name = "custom_max_visitors")
    private Integer customMaxVisitors;

    @Column(name = "custom_open")
    private Boolean customOpen;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots;
}
