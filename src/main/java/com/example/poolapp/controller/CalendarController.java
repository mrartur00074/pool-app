package com.example.poolapp.controller;

import com.example.poolapp.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/pool/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping("/generate")
    public ResponseEntity<Void> generateCalendar() {
        calendarService.generateCalendar(30);
        calendarService.generateWeeklySlots();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/generate-weekly-slots")
    public ResponseEntity<Void> generateWeeklySlots() {
        calendarService.generateWeeklySlots();
        return ResponseEntity.ok().build();
    }
}
