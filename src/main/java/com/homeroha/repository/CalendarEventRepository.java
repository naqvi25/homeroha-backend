package com.homeroha.repository;

import com.homeroha.model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    List<CalendarEvent> findByHomeId(Long homeId);
    List<CalendarEvent> findByCreatedByEmail(String email);
}
