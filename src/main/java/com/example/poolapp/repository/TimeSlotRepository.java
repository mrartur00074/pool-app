package com.example.poolapp.repository;

import com.example.poolapp.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.calendar.date = :date "
            + "ORDER BY ts.startTime ASC")
    List<TimeSlot> findByCalendarDate(@Param("date") LocalDate date);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.calendar.date = :date " +
            "AND ts.isAvailable = true AND ts.currentVisitors < ts.maxVisitors " +
            "ORDER BY ts.startTime ASC")
    List<TimeSlot> findAvailableByCalendarDate(@Param("date") LocalDate date);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.calendar.date = :date " +
            "AND ts.startTime >= :startTime AND ts.startTime < :endTime " +
            "ORDER BY ts.startTime ASC")
    List<TimeSlot> findByDateAndExactTimeRange(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

}
