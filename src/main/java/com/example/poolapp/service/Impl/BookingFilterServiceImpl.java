package com.example.poolapp.service.Impl;

import com.example.poolapp.dto.request.TimeSlotFilterRequest;
import com.example.poolapp.dto.response.BookedSlotResponse;
import com.example.poolapp.model.Booking;
import com.example.poolapp.repository.BookingRepository;
import com.example.poolapp.service.BookingFilterService;
import com.example.poolapp.util.TimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingFilterServiceImpl implements BookingFilterService {
    final BookingRepository bookingRepository;
    final TimeConverter timeConverter;

    public List<BookedSlotResponse> findBookedSlots(TimeSlotFilterRequest filter) {
        List<Booking> bookings;

        if (filter == null) {
            bookings = bookingRepository.findAllActive();
        } else if (filter.hasClientNameOnly()) {
            bookings = bookingRepository.findByClientName(filter.getClientName());
        } else if (filter.hasDateTimeOnly()) {
            LocalDateTime dateTime = timeConverter.parseDateTime(filter.getVisitDate());
            bookings = bookingRepository.findByDateTime(
                    timeConverter.extractDate(dateTime),
                    timeConverter.extractTime(dateTime)
            );
        } else if (filter.hasBothFilters()) {
            LocalDateTime dateTime = timeConverter.parseDateTime(filter.getVisitDate());
            bookings = bookingRepository.findByClientAndDateTime(
                    filter.getClientName(),
                    timeConverter.extractDate(dateTime),
                    timeConverter.extractTime(dateTime)
            );
        } else {
            bookings = bookingRepository.findAllActive();
        }

        return convertToResponseList(bookings);
    }

    private List<BookedSlotResponse> convertToResponseList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private BookedSlotResponse convertToResponse(Booking booking) {
        return new BookedSlotResponse(
                booking.getId(),
                booking.getSlot().getId(),
                booking.getOrder().getClient().getName(),
                timeConverter.convertToLocalDateTime(
                        booking.getSlot().getCalendar().getDate(),
                        booking.getSlot().getStartTime()
                ),
                timeConverter.convertToLocalDateTime(
                        booking.getSlot().getCalendar().getDate(),
                        booking.getSlot().getEndTime()
                ),
                booking.getStatus().name()
        );
    }
}
