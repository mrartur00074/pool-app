package com.example.poolapp.util;

import com.example.poolapp.exception.BusinessLogicException;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class TimeConverter {
    public LocalDateTime convertToLocalDateTime(LocalDate date, Time time) {
        if (date == null || time == null) {
            return null;
        }
        return LocalDateTime.of(date, time.toLocalTime());
    }

    public Time convertToTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Time.valueOf(dateTime.toLocalTime());
    }

    public LocalDate extractDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }

    public Time extractTime(LocalDateTime dateTime) {
        return dateTime != null ? Time.valueOf(dateTime.toLocalTime()) : null;
    }

    public boolean isTimeInSlot(Time slotStart, Time slotEnd, Time targetTime) {
        if (slotStart == null || slotEnd == null || targetTime == null) {
            return false;
        }
        return !targetTime.before(slotStart) && targetTime.before(slotEnd);
    }

    public LocalTime convertTimeToLocalTime(Time time) {
        return time.toLocalTime();
    }

    public LocalDateTime parseDateTime(String datetime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(datetime, formatter);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new BusinessLogicException("Неверный формат даты и времени. Используйте формат: ГГГГ-ММ-ДД ЧЧ:ММ");
        }
    }
}
