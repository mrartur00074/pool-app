package com.example.poolapp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ReserveRequest {
    @NotNull
    private Long clientId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private String datetime;

    @Min(1)
    @Max(8)
    private Integer durationHours = 1;
}
