package com.example.poolapp.repository;

import com.example.poolapp.model.Booking;
import com.example.poolapp.model.Client;
import com.example.poolapp.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByClientAndSlotAndStatus(Client client, TimeSlot slot, Booking.Status status);
    List<Booking> findBySlotAndStatus(TimeSlot slot, Booking.Status status);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.slot s " +
            "JOIN FETCH b.order o " +
            "JOIN FETCH o.client c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :clientName, '%')) " +
            "AND b.status = 'active'")
    List<Booking> findByClientName(@Param("clientName") String clientName);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.slot s " +
            "WHERE s.calendar.date = :date " +
            "AND s.startTime <= :time AND s.endTime > :time " +
            "AND b.status = 'active'")
    List<Booking> findByDateTime(
            @Param("date") LocalDate date,
            @Param("time") Time time);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.slot s " +
            "JOIN FETCH b.order o " +
            "JOIN FETCH o.client c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :clientName, '%')) " +
            "AND s.calendar.date = :date " +
            "AND s.startTime <= :time AND s.endTime > :time " +
            "AND b.status = 'active'")
    List<Booking> findByClientAndDateTime(
            @Param("clientName") String clientName,
            @Param("date") LocalDate date,
            @Param("time") Time time);

    @Query("SELECT b FROM Booking b JOIN FETCH b.slot WHERE b.status = 'active'")
    List<Booking> findAllActive();
}