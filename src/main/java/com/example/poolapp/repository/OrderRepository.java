package com.example.poolapp.repository;

import com.example.poolapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    boolean existsByClientIdAndCreatedAtBetweenAndStatus(
            Long clientId,
            LocalDateTime start,
            LocalDateTime end,
            Order.OrderStatus status);
}
