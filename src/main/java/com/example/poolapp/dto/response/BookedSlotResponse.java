package com.example.poolapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookedSlotResponse {
    private Long bookingId;
    private Long slotId;
    private String clientName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
