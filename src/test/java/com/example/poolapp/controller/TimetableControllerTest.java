package com.example.poolapp.controller;

import com.example.poolapp.dto.request.ReserveRequest;
import com.example.poolapp.dto.request.TimeSlotFilterRequest;
import com.example.poolapp.dto.response.BookedSlotResponse;
import com.example.poolapp.dto.response.CancellationResponse;
import com.example.poolapp.dto.response.OrderResponse;
import com.example.poolapp.dto.response.TimeSlotResponse;
import com.example.poolapp.service.Impl.BookingFacadeImpl;
import com.example.poolapp.service.Impl.BookingFilterServiceImpl;
import com.example.poolapp.service.Impl.TimetableServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimetableControllerTest {

    @Mock
    private TimetableServiceImpl timetableService;

    @Mock
    private BookingFacadeImpl bookingFacade;

    @Mock
    private BookingFilterServiceImpl bookingFilterService;

    @InjectMocks
    private TimetableController timetableController;

    private TimeSlotResponse timeSlotResponse;
    private OrderResponse orderResponse;
    private CancellationResponse cancellationResponse;
    private BookedSlotResponse bookedSlotResponse;


    @BeforeEach
    void setUp() {
        timeSlotResponse = new TimeSlotResponse();
        timeSlotResponse.setSlotId(1L);
        timeSlotResponse.setAvailable(true);

        String startTimeString = "10:00";
        String endTimeString = "10:00";

        LocalTime startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(endTimeString, DateTimeFormatter.ofPattern("HH:mm"));

        LocalDate date = LocalDate.now();

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        orderResponse = new OrderResponse(UUID.randomUUID(), LocalDateTime.now(), "Success", new ArrayList<>());
        cancellationResponse = new CancellationResponse(true, "Cancelled", LocalDateTime.now(), UUID.randomUUID(), new ArrayList<>());
        bookedSlotResponse = new BookedSlotResponse(1L, 1L, "Artur", startDateTime, endDateTime, "active");
    }

    @Test
    void getAllTimeSlotsForDate_ReturnsSlots() {
        when(timetableService.getAllTimeSlotsForDate(any(LocalDate.class)))
                .thenReturn(Arrays.asList(timeSlotResponse));

        ResponseEntity<List<TimeSlotResponse>> response =
                timetableController.getAllTimeSlotsForDate(LocalDate.now());

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1L, response.getBody().get(0).getSlotId());
    }

    @Test
    void getAvailableTimeSlotsForDate_ReturnsAvailableSlots() {
        when(timetableService.getAvailableTimeSlotsForDate(any(LocalDate.class)))
                .thenReturn(Arrays.asList(timeSlotResponse));

        ResponseEntity<List<TimeSlotResponse>> response =
                timetableController.getAvailableTimeSlotsForDate(LocalDate.now());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().get(0).isAvailable());
    }

    @Test
    void reserveTimeSlot_ReturnsOrderResponse() {
        ReserveRequest request = new ReserveRequest();
        when(bookingFacade.reserveTimeSlot(any(ReserveRequest.class)))
                .thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response =
                timetableController.reserveTimeSlot(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getOrderId());
    }

    @Test
    void cancelBooking_ReturnsCancellationResponse() {
        when(bookingFacade.cancelBooking(anyLong(), any(UUID.class)))
                .thenReturn(cancellationResponse);

        ResponseEntity<CancellationResponse> response =
                timetableController.cancelBooking(1L, UUID.randomUUID());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void filterBookings_ReturnsBookedSlots() {
        TimeSlotFilterRequest filter = new TimeSlotFilterRequest();
        when(bookingFilterService.findBookedSlots(any(TimeSlotFilterRequest.class)))
                .thenReturn(Arrays.asList(bookedSlotResponse));

        ResponseEntity<List<BookedSlotResponse>> response =
                timetableController.filterBookings(filter);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}
