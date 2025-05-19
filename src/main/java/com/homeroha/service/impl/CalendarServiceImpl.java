package com.homeroha.service.impl;
import com.homeroha.dto.Role;

import com.homeroha.dto.CalendarEventDTO;
import com.homeroha.model.CalendarEvent;
import com.homeroha.repository.CalendarEventRepository;
import com.homeroha.repository.UserRepository;
import com.homeroha.service.CalendarService;
import com.homeroha.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CalendarEventRepository repository;
    private final UserRepository userRepository;
    private final UserService userService;

    private CalendarEventDTO mapToDTO(CalendarEvent event) {
        CalendarEventDTO dto = new CalendarEventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setVisibility(event.getVisibility());
        dto.setSharedWithUserIds(event.getSharedWithUserIds());
        dto.setHiddenFromUserIds(event.getHiddenFromUserIds());
        dto.setHomeId(event.getHomeId());
        dto.setRecurring(event.isRecurring());
        dto.setRecurrenceType(event.getRecurrenceType());
        return dto;
    }

    private CalendarEvent mapToEntity(CalendarEventDTO dto, String createdByEmail) {
        return CalendarEvent.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .visibility(dto.getVisibility())
                .sharedWithUserIds(dto.getSharedWithUserIds())
                .hiddenFromUserIds(dto.getHiddenFromUserIds())
                .homeId(dto.getHomeId())
                .createdByEmail(createdByEmail)
                .isRecurring(dto.isRecurring())
                .recurrenceType(dto.getRecurrenceType())
                .build();
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @Override
    public Role getUserRoleByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getRole();
    }

    @Override
    public CalendarEventDTO addEvent(String email, CalendarEventDTO dto) {
        Long creatorUserId = userService.getUserIdByEmail(email);

        // 🛠️ Auto-share with self if no one selected
        if ("SHARED".equals(dto.getVisibility()) && (dto.getSharedWithUserIds() == null || dto.getSharedWithUserIds().isEmpty())) {
            dto.setSharedWithUserIds(List.of(creatorUserId));
        }

        CalendarEvent saved = repository.save(mapToEntity(dto, email));
        return mapToDTO(saved);
    }

    @Override
    public List<CalendarEventDTO> getVisibleEvents(String email, Long homeId) {
        Long userId = getUserIdByEmail(email); // Resolve userId from email

        return repository.findByHomeId(homeId).stream()
                .filter(event -> {
                    // PUBLIC: visible to all
                    if ("PUBLIC".equals(event.getVisibility())) {
                        return true;
                    }

                    // PRIVATE: visible only to creator
                    if ("PRIVATE".equals(event.getVisibility()) && email.equals(event.getCreatedByEmail())) {
                        return true;
                    }

                    // SHARED: visible to selected userIds
                    if ("SHARED".equals(event.getVisibility()) && event.getSharedWithUserIds() != null) {
                        return event.getSharedWithUserIds().contains(userId);
                    }

                    return false;
                })
                .filter(event -> {
                    List<Long> hidden = event.getHiddenFromUserIds();
                    return hidden == null || !hidden.contains(userId);
                })
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<CalendarEventDTO> getMyEvents(String email) {
        return repository.findByCreatedByEmail(email).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CalendarEventDTO updateEvent(String email, Long id, CalendarEventDTO dto) {
        CalendarEvent event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Role role = getUserRoleByEmail(email);
        if (!event.getCreatedByEmail().equals(email) && role != Role.ADMIN) {
            throw new RuntimeException("Only creator or admin can modify this event");
        }


        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setVisibility(dto.getVisibility());
        event.setSharedWithUserIds(dto.getSharedWithUserIds());
        event.setHiddenFromUserIds(dto.getHiddenFromUserIds());
        event.setRecurring(dto.isRecurring());
        event.setRecurrenceType(dto.getRecurrenceType());

        return mapToDTO(repository.save(event));
    }

    @Override
    public void deleteEvent(String email, Long id) {
        CalendarEvent event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Role role = getUserRoleByEmail(email);
        if (!event.getCreatedByEmail().equals(email) && role != Role.ADMIN) {
            throw new RuntimeException("Only creator or admin can modify this event");
        }
        repository.delete(event);
    }

    @Override
    public CalendarEventDTO getEventById(String email, Long id) {
        CalendarEvent event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapToDTO(event);
    }
}
