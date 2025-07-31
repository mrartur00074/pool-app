package com.example.poolapp.service;

import com.example.poolapp.dto.request.ReserveRequest;
import com.example.poolapp.dto.response.CancellationResponse;
import com.example.poolapp.dto.response.OrderResponse;

import java.util.UUID;

public interface BookingFacade {
    CancellationResponse cancelBooking(Long clientId, UUID orderId);
    OrderResponse reserveTimeSlot(ReserveRequest request);
}
