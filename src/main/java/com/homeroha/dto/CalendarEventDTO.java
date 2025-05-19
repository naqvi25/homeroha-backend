package com.homeroha.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CalendarEventDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String visibility;
    private List<Long> sharedWithUserIds;
    private List<Long> hiddenFromUserIds;

    private Long homeId;
    private boolean isRecurring;
    private String recurrenceType;
}
