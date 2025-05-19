package com.homeroha.controller;

import com.homeroha.dto.CalendarEventDTO;
import com.homeroha.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("/add")
    public ResponseEntity<CalendarEventDTO> addEvent(@RequestBody CalendarEventDTO dto, Principal principal) {
        return ResponseEntity.ok(calendarService.addEvent(principal.getName(), dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<CalendarEventDTO>> getEvents(@RequestParam Long homeId, Principal principal) {
        return ResponseEntity.ok(calendarService.getVisibleEvents(principal.getName(), homeId));
    }

    @GetMapping("/my-events")
    public ResponseEntity<List<CalendarEventDTO>> getMyEvents(Principal principal) {
        return ResponseEntity.ok(calendarService.getMyEvents(principal.getName()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CalendarEventDTO> updateEvent(@PathVariable Long id, @RequestBody CalendarEventDTO dto, Principal principal) {
        return ResponseEntity.ok(calendarService.updateEvent(principal.getName(), id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id, Principal principal) {
        calendarService.deleteEvent(principal.getName(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarEventDTO> getEvent(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(calendarService.getEventById(principal.getName(), id));
    }
}
