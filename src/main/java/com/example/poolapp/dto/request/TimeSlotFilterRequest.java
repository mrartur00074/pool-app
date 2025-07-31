package com.example.poolapp.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class TimeSlotFilterRequest {
    private String clientName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private String visitDate;

    public boolean hasClientNameOnly() {
        return clientName != null && visitDate == null;
    }

    public boolean hasDateTimeOnly() {
        return visitDate != null && clientName == null;
    }

    public boolean hasBothFilters() {
        return clientName != null && visitDate != null;
    }
}
