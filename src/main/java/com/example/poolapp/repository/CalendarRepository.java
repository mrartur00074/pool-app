package com.example.poolapp.repository;

import com.example.poolapp.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, LocalDate> {

}
