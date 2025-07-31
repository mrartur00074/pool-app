package com.example.poolapp.service;

import com.example.poolapp.dto.response.OrderResponse;
import com.example.poolapp.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrderWithBookings(Long clientId, List<Long> slotIds);
    Order cancelOrder(Long clientId, UUID orderId);
}
