package com.example.poolapp.controller;

import com.example.poolapp.dto.request.ReserveRequest;
import com.example.poolapp.dto.request.TimeSlotFilterRequest;
import com.example.poolapp.dto.response.BookedSlotResponse;
import com.example.poolapp.dto.response.CancellationResponse;
import com.example.poolapp.dto.response.OrderResponse;
import com.example.poolapp.dto.response.TimeSlotResponse;
import com.example.poolapp.service.Impl.BookingFacadeImpl;
import com.example.poolapp.service.Impl.BookingFilterServiceImpl;
import com.example.poolapp.service.Impl.TimetableServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v0/pool/timetable")
@RequiredArgsConstructor
public class TimetableController {
    final TimetableServiceImpl timetableService;
    final BookingFacadeImpl bookingFacade;
    final BookingFilterServiceImpl bookingFilterService;

    @GetMapping("/all")
    public ResponseEntity<List<TimeSlotResponse>> getAllTimeSlotsForDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(timetableService.getAllTimeSlotsForDate(date));
    }

    @GetMapping("/available")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableTimeSlotsForDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(timetableService.getAvailableTimeSlotsForDate(date));
    }

    @PostMapping("/reserve")
    public ResponseEntity<OrderResponse> reserveTimeSlot(@RequestBody ReserveRequest request) {
        return ResponseEntity.ok(bookingFacade.reserveTimeSlot(request));
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<CancellationResponse> cancelBooking(
            @RequestParam Long clientId,
            @RequestParam UUID orderId) {

        return ResponseEntity.ok(bookingFacade.cancelBooking(clientId, orderId));
    }

    @PostMapping("/filter")
    public ResponseEntity<List<BookedSlotResponse>> filterBookings(
            @Valid @RequestBody TimeSlotFilterRequest filter) {

        return ResponseEntity.ok(bookingFilterService.findBookedSlots(filter));
    }
}
