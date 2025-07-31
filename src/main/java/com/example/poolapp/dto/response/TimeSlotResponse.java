package com.example.poolapp.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class TimeSlotResponse {
    private Long slotId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxVisitors;
    private int currentVisitors;
    private boolean available;
    private List<ClientShortDto> clients;
}
