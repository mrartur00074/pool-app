package com.example.poolapp.util;

import com.example.poolapp.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TimeConverterTest {

    private final TimeConverter timeConverter = new TimeConverter();

    @Test
    void convertToLocalDateTime_WithValidInput_ReturnsCorrectDateTime() {
        LocalDate date = LocalDate.of(2023, 1, 15);
        Time time = Time.valueOf("10:30:00");

        LocalDateTime result = timeConverter.convertToLocalDateTime(date, time);

        assertEquals(LocalDateTime.of(2023, 1, 15, 10, 30), result);
    }

    @Test
    void convertToLocalDateTime_WithNullInput_ReturnsNull() {
        assertNull(timeConverter.convertToLocalDateTime(null, Time.valueOf("10:30:00")));
        assertNull(timeConverter.convertToLocalDateTime(LocalDate.now(), null));
        assertNull(timeConverter.convertToLocalDateTime(null, null));
    }

    @Test
    void convertToTime_WithValidInput_ReturnsCorrectTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 15, 10, 30);
        Time result = timeConverter.convertToTime(dateTime);

        assertEquals(Time.valueOf("10:30:00"), result);
    }

    @Test
    void convertToTime_WithNullInput_ReturnsNull() {
        assertNull(timeConverter.convertToTime(null));
    }

    @Test
    void extractDate_WithValidInput_ReturnsCorrectDate() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 15, 10, 30);
        LocalDate result = timeConverter.extractDate(dateTime);

        assertEquals(LocalDate.of(2023, 1, 15), result);
    }

    @Test
    void extractDate_WithNullInput_ReturnsNull() {
        assertNull(timeConverter.extractDate(null));
    }

    @Test
    void extractTime_WithValidInput_ReturnsCorrectTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 15, 10, 30);
        Time result = timeConverter.extractTime(dateTime);

        assertEquals(Time.valueOf("10:30:00"), result);
    }

    @Test
    void extractTime_WithNullInput_ReturnsNull() {
        assertNull(timeConverter.extractTime(null));
    }

    @Test
    void isTimeInSlot_WithTimeInsideSlot_ReturnsTrue() {
        Time slotStart = Time.valueOf("10:00:00");
        Time slotEnd = Time.valueOf("11:00:00");
        Time targetTime = Time.valueOf("10:30:00");

        assertTrue(timeConverter.isTimeInSlot(slotStart, slotEnd, targetTime));
    }

    @Test
    void isTimeInSlot_WithTimeAtStart_ReturnsTrue() {
        Time slotStart = Time.valueOf("10:00:00");
        Time slotEnd = Time.valueOf("11:00:00");
        Time targetTime = Time.valueOf("10:00:00");

        assertTrue(timeConverter.isTimeInSlot(slotStart, slotEnd, targetTime));
    }

    @Test
    void isTimeInSlot_WithTimeAtEnd_ReturnsFalse() {
        Time slotStart = Time.valueOf("10:00:00");
        Time slotEnd = Time.valueOf("11:00:00");
        Time targetTime = Time.valueOf("11:00:00");

        assertFalse(timeConverter.isTimeInSlot(slotStart, slotEnd, targetTime));
    }

    @Test
    void isTimeInSlot_WithNullInput_ReturnsFalse() {
        assertFalse(timeConverter.isTimeInSlot(null, Time.valueOf("10:00:00"), Time.valueOf("10:30:00")));
        assertFalse(timeConverter.isTimeInSlot(Time.valueOf("10:00:00"), null, Time.valueOf("10:30:00")));
        assertFalse(timeConverter.isTimeInSlot(Time.valueOf("10:00:00"), Time.valueOf("11:00:00"), null));
    }

    @Test
    void convertTimeToLocalTime_WithValidInput_ReturnsCorrectLocalTime() {
        Time time = Time.valueOf("10:30:00");
        LocalTime result = timeConverter.convertTimeToLocalTime(time);

        assertEquals(LocalTime.of(10, 30), result);
    }

    @Test
    void parseDateTime_WithValidInput_ReturnsCorrectDateTime() {
        String datetime = "2023-01-15 10:30";
        LocalDateTime result = timeConverter.parseDateTime(datetime);

        assertEquals(LocalDateTime.of(2023, 1, 15, 10, 30), result);
    }

    @Test
    void parseDateTime_WithInvalidFormat_ThrowsBusinessLogicException() {
        String invalidDatetime = "2023/01/15 10-30";

        assertThrows(BusinessLogicException.class, () -> timeConverter.parseDateTime(invalidDatetime));
    }

    @Test
    void parseDateTime_WithNullInput_ThrowsBusinessLogicException() {
        assertThrows(BusinessLogicException.class, () -> timeConverter.parseDateTime(null));
    }
}