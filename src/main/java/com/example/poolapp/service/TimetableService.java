package com.example.poolapp.service;

import com.example.poolapp.dto.response.TimeSlotResponse;
import com.example.poolapp.model.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface TimetableService {
    List<TimeSlotResponse> getAllTimeSlotsForDate(LocalDate date);
    List<TimeSlotResponse> getAvailableTimeSlotsForDate(LocalDate date);
    List<TimeSlot> validateAndGetAvailableSlots(String datetime, int durationHours);
}
