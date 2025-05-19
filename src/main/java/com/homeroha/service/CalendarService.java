package com.homeroha.service;

import com.homeroha.dto.CalendarEventDTO;

import java.util.List;
import com.homeroha.dto.Role;

public interface CalendarService {
    CalendarEventDTO addEvent(String email, CalendarEventDTO dto);
    List<CalendarEventDTO> getVisibleEvents(String email, Long homeId);
    List<CalendarEventDTO> getMyEvents(String email);
    CalendarEventDTO updateEvent(String email, Long id, CalendarEventDTO dto);
    void deleteEvent(String email, Long id);
    CalendarEventDTO getEventById(String email, Long id);
    Long getUserIdByEmail(String email);
    Role getUserRoleByEmail(String email);
}
