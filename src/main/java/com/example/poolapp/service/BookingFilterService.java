package com.example.poolapp.service;

import com.example.poolapp.dto.request.TimeSlotFilterRequest;
import com.example.poolapp.dto.response.BookedSlotResponse;

import java.util.List;

public interface BookingFilterService {
    List<BookedSlotResponse> findBookedSlots(TimeSlotFilterRequest filter);
}
