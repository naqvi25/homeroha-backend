// File: model/SharedContact.java
package com.homeroha.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class SharedContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    private List<String> phoneNumbers;

    private String email;

    private String category;

    @ElementCollection
    private List<String> tags;

    private String notes;

    private String location;

    private Boolean isVerified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    private User addedBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    private Home home;
}