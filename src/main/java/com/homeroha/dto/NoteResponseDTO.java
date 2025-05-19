package com.homeroha.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoteResponseDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime reminderTime;
    private String createdBy;
}