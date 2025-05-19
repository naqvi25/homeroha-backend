package com.homeroha.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteRequestDTO {
    private String title;
    private String content;
    private LocalDateTime reminderTime;
}
