package com.homeroha.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String visibility; // PRIVATE, SHARED, PUBLIC

    @ElementCollection
    private List<Long> sharedWithUserIds;

    @ElementCollection
    private List<Long> hiddenFromUserIds;

    private Long homeId;

    private String createdByEmail;

    private boolean isRecurring;
    private String recurrenceType; // DAILY, WEEKLY, MONTHLY, YEARLY, NONE
}
