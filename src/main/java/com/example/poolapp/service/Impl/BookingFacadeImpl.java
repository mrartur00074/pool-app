package com.example.poolapp.service.Impl;

import com.example.poolapp.dto.request.ReserveRequest;
import com.example.poolapp.dto.response.CancellationResponse;
import com.example.poolapp.dto.response.OrderResponse;
import com.example.poolapp.model.Client;
import com.example.poolapp.model.Order;
import com.example.poolapp.model.TimeSlot;
import com.example.poolapp.service.BookingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingFacadeImpl implements BookingFacade {
    private final TimetableServiceImpl timetableService;
    private final OrderServiceImpl orderService;
    private final ClientServiceImpl clientService;

    @Override
    @Transactional
    public CancellationResponse cancelBooking(Long clientId, UUID orderId) {
        clientService.getClientOrThrow(clientId);

        Order cancelledOrder = orderService.cancelOrder(clientId, orderId);

        List<CancellationResponse.CancelledBookingInfo> cancelledBookings =
                cancelledOrder.getBookings().stream()
                        .map(booking -> new CancellationResponse.CancelledBookingInfo(
                                booking.getId(),
                                booking.getSlot().getId(),
                                formatSlotTime(booking.getSlot()),
                                booking.getStatus().name()
                        ))
                        .collect(Collectors.toList());

        return new CancellationResponse(
                true,
                "Все бронирования в заказе отменены",
                LocalDateTime.now(),
                cancelledOrder.getOrder_id(),
                cancelledBookings
        );
    }

    @Override
    @Transactional
    public OrderResponse reserveTimeSlot(ReserveRequest request) {
        Client client = clientService.getClientOrThrow(request.getClientId());

        List<TimeSlot> availableSlots = timetableService.validateAndGetAvailableSlots(
                request.getDatetime(),
                request.getDurationHours()
        );

        return orderService.createOrderWithBookings(client.getId(), extractSlotIds(availableSlots));
    }

    private List<Long> extractSlotIds(List<TimeSlot> slots) {
        return slots.stream()
                .map(TimeSlot::getId)
                .collect(Collectors.toList());
    }

    private String formatSlotTime(TimeSlot slot) {
        return slot.getStartTime().toLocalTime() + " - " + slot.getEndTime().toLocalTime();
    }
}
