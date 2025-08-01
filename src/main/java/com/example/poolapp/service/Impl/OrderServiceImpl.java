package com.example.poolapp.service.Impl;

import com.example.poolapp.dto.response.OrderResponse;
import com.example.poolapp.exception.BusinessLogicException;
import com.example.poolapp.exception.ResourceNotFoundException;
import com.example.poolapp.model.Booking;
import com.example.poolapp.model.Client;
import com.example.poolapp.model.Order;
import com.example.poolapp.model.TimeSlot;
import com.example.poolapp.repository.BookingRepository;
import com.example.poolapp.repository.ClientRepository;
import com.example.poolapp.repository.OrderRepository;
import com.example.poolapp.repository.TimeSlotRepository;
import com.example.poolapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public OrderResponse createOrderWithBookings(Long clientId, List<Long> slotIds) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент не найден"));

        TimeSlot firstSlot = timeSlotRepository.findById(slotIds.get(0))
                .orElseThrow(() -> new ResourceNotFoundException("Слот не найден"));

        validateNoExistingOrdersForVisitDate(clientId, firstSlot.getCalendar().getDate());

        Order order = new Order();
        order.setClient(client);

        for (Long slotId : slotIds) {
            TimeSlot slot = timeSlotRepository.findById(slotId)
                    .orElseThrow(() -> new ResourceNotFoundException("Слот не найден"));

            if (bookingRepository.existsByClientAndSlotAndStatus(client, slot, Booking.Status.active)) {
                throw new BusinessLogicException("У вас уже есть активное бронирование на этот слот");
            }

            Booking booking = new Booking();
            booking.setClient(client);
            booking.setSlot(slot);
            booking.setStatus(Booking.Status.active);
            order.addBooking(booking);
        }

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponse(savedOrder);
    }

    /*private void validateNoActiveOrdersToday(Long clientId) {
        LocalDate today = LocalDate.now();
        boolean hasActiveOrder = orderRepository.existsByClientIdAndCreatedAtBetweenAndStatus(
                clientId,
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay(),
                Order.OrderStatus.ACTIVE
        );
        if (hasActiveOrder) {
            throw new BusinessLogicException("Разрешён только один активный заказ в день");
        }
    }*/

    private void validateNoExistingOrdersForVisitDate(Long clientId, LocalDate visitDate) {
        boolean hasExistingOrder = orderRepository.existsActiveOrderForDate(
                clientId,
                visitDate
        );
        if (hasExistingOrder) {
            throw new BusinessLogicException("Вы уже записаны на эту дату посещения");
        }
    }

    private OrderResponse convertToOrderResponse(Order order) {
        List<OrderResponse.BookingInfo> bookingInfos = order.getBookings().stream()
                .map(booking -> new OrderResponse.BookingInfo(
                        booking.getId(),
                        booking.getSlot().getId(),
                        booking.getSlot().getStartTime().toLocalTime(),
                        booking.getSlot().getEndTime().toLocalTime(),
                        booking.getStatus().toString()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getOrder_id(),
                order.getCreatedAt(),
                order.getStatus().name(),
                bookingInfos
        );
    }


    @Override
    @Transactional
    public Order cancelOrder(Long clientId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ не найден"));

        if (!order.getClient().getId().equals(clientId)){
            throw new BusinessLogicException("Нельзя отменить чужой заказ");
        }

        if (order.getStatus() != Order.OrderStatus.ACTIVE) {
            throw new BusinessLogicException("Нельзя отменить завершённый или отменённый заказ");
        }

        order.getBookings().forEach(booking -> {
            booking.setStatus(Booking.Status.cancelled);
        });

        List<TimeSlot> slotsToUpdate = order.getBookings().stream()
                .map(Booking::getSlot)
                .collect(Collectors.toList());

        timeSlotRepository.saveAll(slotsToUpdate);

        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}
