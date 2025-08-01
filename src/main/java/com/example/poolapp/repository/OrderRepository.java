package com.example.poolapp.repository;

import com.example.poolapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM Order o JOIN o.bookings b JOIN b.slot t JOIN t.calendar c " +
            "WHERE o.client.id = :clientId AND c.date = :visitDate AND o.status = 'ACTIVE'")
    boolean existsActiveOrderForDate(
            @Param("clientId") Long clientId,
            @Param("visitDate") LocalDate visitDate
    );
}
