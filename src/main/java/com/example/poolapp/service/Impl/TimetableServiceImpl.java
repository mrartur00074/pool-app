package com.example.poolapp.service.Impl;

import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.dto.response.TimeSlotResponse;
import com.example.poolapp.exception.BusinessLogicException;
import com.example.poolapp.model.Booking;
import com.example.poolapp.model.TimeSlot;
import com.example.poolapp.repository.BookingRepository;
import com.example.poolapp.repository.TimeSlotRepository;
import com.example.poolapp.service.TimetableService;
import com.example.poolapp.util.TimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    private final TimeSlotRepository timeSlotRepository;
    private final BookingRepository bookingRepository;
    private final TimeConverter timeConverter;

    @Override
    public List<TimeSlotResponse> getAllTimeSlotsForDate(LocalDate date) {
        return timeSlotRepository.findByCalendarDate(date).stream()
                .map(this::convertToTimeSlotResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeSlotResponse> getAvailableTimeSlotsForDate(LocalDate date) {
        List<TimeSlot> availableSlots = timeSlotRepository
                .findAvailableByCalendarDate(date);

        return availableSlots.stream()
                .map(this::convertToTimeSlotResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeSlot> validateAndGetAvailableSlots(String datetime, int durationHours) {
        LocalDateTime dateTime = timeConverter.parseDateTime(datetime);
        LocalDate date = dateTime.toLocalDate();
        LocalTime startTime = dateTime.toLocalTime();
        LocalTime endTime = startTime.plusHours(durationHours);

        List<TimeSlot> allDaySlots = timeSlotRepository.findByCalendarDate(date);
        if (allDaySlots.isEmpty()) {
            throw new BusinessLogicException("На эту дату нет доступных слотов");
        }

        validatePoolWorkingHours(allDaySlots, startTime, endTime);

        List<TimeSlot> slots = timeSlotRepository.findByDateAndExactTimeRange(date, startTime, endTime);
        if (slots.size() != durationHours) {
            throw new BusinessLogicException("Нельзя забронировать указанный период - нет подходящих слотов");
        }

        validateSlotsContinuity(slots);

        slots.forEach(slot -> {
            if (!slot.getIsAvailable() || slot.getCurrentVisitors() >= slot.getMaxVisitors()) {
                throw new BusinessLogicException(
                        String.format("Слот %s-%s недоступен", slot.getStartTime(), slot.getEndTime()));
            }
        });

        return slots;
    }

    private void validatePoolWorkingHours(List<TimeSlot> allDaySlots, LocalTime startTime, LocalTime endTime) {
        LocalTime poolOpensAt = allDaySlots.get(0).getStartTime().toLocalTime();
        LocalTime poolClosesAt = allDaySlots.get(allDaySlots.size()-1).getEndTime().toLocalTime();

        if (startTime.isBefore(poolOpensAt) || endTime.isAfter(poolClosesAt)) {
            throw new BusinessLogicException(
                    String.format("Бассейн работает с %s до %s", poolOpensAt, poolClosesAt));
        }
    }

    private void validateSlotsContinuity(List<TimeSlot> slots) {
        for (int i = 1; i < slots.size(); i++) {
            if (!slots.get(i-1).getEndTime().equals(slots.get(i).getStartTime())) {
                throw new BusinessLogicException("В выбранном периоде есть временные пропуски");
            }
        }
    }

    private TimeSlotResponse convertToTimeSlotResponse(TimeSlot slot) {
        TimeSlotResponse response = new TimeSlotResponse();
        response.setSlotId(slot.getId());
        response.setDate(slot.getCalendar().getDate());
        response.setStartTime(timeConverter.convertTimeToLocalTime(slot.getStartTime()));
        response.setEndTime(timeConverter.convertTimeToLocalTime(slot.getEndTime()));
        response.setMaxVisitors(slot.getMaxVisitors());
        response.setCurrentVisitors(slot.getCurrentVisitors());
        response.setAvailable(slot.getIsAvailable() && slot.getCurrentVisitors() < slot.getMaxVisitors());

        List<ClientShortDto> clients = bookingRepository.findBySlotAndStatus(slot, Booking.Status.active).stream()
                .map(b -> new ClientShortDto(b.getClient().getId(), b.getClient().getName()))
                .collect(Collectors.toList());

        response.setClients(clients);

        return response;
    }

}
