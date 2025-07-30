package com.example.poolapp.service.Impl;

import com.example.poolapp.model.Calendar;
import com.example.poolapp.model.DayTemplate;
import com.example.poolapp.repository.CalendarRepository;
import com.example.poolapp.repository.DayTemplateRepository;
import com.example.poolapp.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;
    private final DayTemplateRepository dayTemplateRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void generateCalendar(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (calendarRepository.findById(date).isEmpty()) {
                Calendar calendar = new Calendar();
                calendar.setDate(date);

                DayTemplate template = switch (date.getDayOfWeek()) {
                    case SATURDAY -> dayTemplateRepository.findById(3L).orElseThrow();
                    case SUNDAY -> dayTemplateRepository.findById(2L).orElseThrow();
                    default -> dayTemplateRepository.findById(1L).orElseThrow();
                };

                calendar.setTemplate(template);
                calendarRepository.save(calendar);
            }
        }
    }

    @Transactional
    public void generateWeeklySlots() {
        jdbcTemplate.update("CALL generate_weekly_slots()");
    }
}
