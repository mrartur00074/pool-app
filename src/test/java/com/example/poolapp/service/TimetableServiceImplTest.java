package com.example.poolapp.service;

import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.dto.response.TimeSlotResponse;
import com.example.poolapp.exception.BusinessLogicException;
import com.example.poolapp.model.Booking;
import com.example.poolapp.model.Calendar;
import com.example.poolapp.model.TimeSlot;
import com.example.poolapp.repository.BookingRepository;
import com.example.poolapp.repository.TimeSlotRepository;
import com.example.poolapp.service.Impl.TimetableServiceImpl;
import com.example.poolapp.util.TimeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimetableServiceImplTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TimeConverter timeConverter;

    @InjectMocks
    private TimetableServiceImpl timetableService;

    private TimeSlot timeSlot;
    private Booking booking;

    @BeforeEach
    void setUp() {
        Calendar calendar = new Calendar();
        calendar.setDate(LocalDate.of(2023, 1, 1));

        timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setCalendar(calendar);
        timeSlot.setStartTime(Time.valueOf(LocalDateTime.of(2023, 1, 1, 10, 0).toLocalTime()));
        timeSlot.setEndTime(Time.valueOf(LocalDateTime.of(2023, 1, 1, 11, 0).toLocalTime()));
        timeSlot.setMaxVisitors(10);
        timeSlot.setCurrentVisitors(5);
        timeSlot.setIsAvailable(true);

        booking = new Booking();
        booking.setClient(new com.example.poolapp.model.Client(1L, "John", "john@test.com", "+12312312312", LocalDateTime.now(), new ArrayList<>()));
    }

    @Test
    void getAllTimeSlotsForDate_ReturnsSlots() {
        when(timeSlotRepository.findByCalendarDate(any(LocalDate.class)))
                .thenReturn(Arrays.asList(timeSlot));
        when(bookingRepository.findBySlotAndStatus(any(), any()))
                .thenReturn(Arrays.asList(booking));

        List<TimeSlotResponse> result =
                timetableService.getAllTimeSlotsForDate(LocalDate.now());

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getSlotId());
    }

    @Test
    void getAvailableTimeSlotsForDate_ReturnsAvailableSlots() {
        when(timeSlotRepository.findAvailableByCalendarDate(any(LocalDate.class)))
                .thenReturn(Arrays.asList(timeSlot));
        when(bookingRepository.findBySlotAndStatus(any(), any()))
                .thenReturn(Arrays.asList(booking));

        List<TimeSlotResponse> result =
                timetableService.getAvailableTimeSlotsForDate(LocalDate.now());

        assertTrue(result.get(0).isAvailable());
    }

    /*@Test
    void validateAndGetAvailableSlots_ReturnsValidSlots() {
        Calendar calendar2 = new Calendar();
        calendar2.setDate(LocalDate.of(2023, 1, 1));

        TimeSlot timeSlot2 = new TimeSlot();
        timeSlot2.setId(1L);
        timeSlot2.setCalendar(calendar2);
        timeSlot2.setStartTime(Time.valueOf(LocalDateTime.of(2023, 1, 1, 11, 0).toLocalTime()));
        timeSlot2.setEndTime(Time.valueOf(LocalDateTime.of(2023, 1, 1, 12, 0).toLocalTime()));
        timeSlot2.setMaxVisitors(10);
        timeSlot2.setCurrentVisitors(5);
        timeSlot2.setIsAvailable(true);

        when(timeSlotRepository.findByCalendarDate(any(LocalDate.class)))
                .thenReturn(Arrays.asList(timeSlot, timeSlot2));
        when(timeSlotRepository.findByDateAndExactTimeRange(any(), any(), any()))
                .thenReturn(Arrays.asList(timeSlot, timeSlot2));
        when(timeConverter.parseDateTime(anyString()))
                .thenReturn(LocalDateTime.now());

        List<TimeSlot> result =
                timetableService.validateAndGetAvailableSlots("2023-01-01 10:00", 1);

        assertEquals(1, result.size());
    }*/



    @Test
    void validateAndGetAvailableSlots_ThrowsWhenPoolClosed() {
        when(timeSlotRepository.findByCalendarDate(any(LocalDate.class)))
                .thenReturn(Arrays.asList(timeSlot));
        when(timeConverter.parseDateTime(anyString()))
                .thenReturn(LocalDateTime.of(2023, 1, 1, 9, 0));

        assertThrows(BusinessLogicException.class,
                () -> timetableService.validateAndGetAvailableSlots("2023-01-01 09:00", 1));
    }

    @Test
    void validateSlotsContinuity_ThrowsWhenGapsExist() {
        TimeSlot slot1 = new TimeSlot();
        slot1.setEndTime(Time.valueOf(LocalDateTime.of(2023, 1, 1, 10, 30).toLocalTime()));

        TimeSlot slot2 = new TimeSlot();
        slot2.setStartTime(Time.valueOf(LocalDateTime.of(2023, 1, 1, 10, 45).toLocalTime()));

        assertThrows(BusinessLogicException.class,
                () -> timetableService.validateSlotsContinuity(Arrays.asList(slot1, slot2)));
    }

    @Test
    void convertToTimeSlotResponse_ConvertsCorrectly() {
        when(bookingRepository.findBySlotAndStatus(any(), any()))
                .thenReturn(Arrays.asList(booking));
        when(timeConverter.convertTimeToLocalTime(any()))
                .thenReturn(LocalTime.parse("10:00", DateTimeFormatter.ofPattern("HH:mm")));

        TimeSlotResponse response = timetableService.convertToTimeSlotResponse(timeSlot);

        assertEquals(1L, response.getSlotId());
        assertEquals(1, response.getClients().size());
    }
}
