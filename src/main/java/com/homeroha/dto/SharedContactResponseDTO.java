package com.homeroha.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SharedContactResponseDTO {
    private Long id;
    private String name;
    private List<String> phoneNumbers;
    private String email;
    private String category;
    private List<String> tags;
    private String notes;
    private String location;
    private Boolean isVerified;
    private String addedBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}