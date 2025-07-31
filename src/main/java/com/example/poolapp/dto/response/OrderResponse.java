package com.example.poolapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private LocalDateTime createdAt;
    private String status;
    private List<BookingInfo> bookings;

    @Data
    @AllArgsConstructor
    public static class BookingInfo {
        private Long bookingId;
        private Long slotId;
        private LocalTime startTime;
        private LocalTime endTime;
        private String status;
    }
}
