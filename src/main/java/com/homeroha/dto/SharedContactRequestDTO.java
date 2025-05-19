// File: dto/SharedContactRequestDTO.java
package com.homeroha.dto;

import lombok.Data;
import java.util.List;

@Data
public class SharedContactRequestDTO {
    private Long homeId;
    private String name;
    private List<String> phoneNumbers;
    private String email;
    private String category;
    private List<String> tags;
    private String notes;
    private String location;
    private Boolean isVerified;
}