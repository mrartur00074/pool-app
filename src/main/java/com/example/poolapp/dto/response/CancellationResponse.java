package com.example.poolapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CancellationResponse {
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private UUID orderId;
    private List<CancelledBookingInfo> cancelledBookings;

    @Data
    @AllArgsConstructor
    public static class CancelledBookingInfo {
        private Long bookingId;
        private Long slotId;
        private String slotTime;
        private String status;
    }
}
