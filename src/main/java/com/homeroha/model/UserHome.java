package com.homeroha.model;

import com.homeroha.dto.Role;
import com.homeroha.dto.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_home")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserHome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for this link

    // Relation to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who is part of the home

    // Relation to Home
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home; // The home they belong to

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, MEMBER, GUEST

    @Enumerated(EnumType.STRING)
    private Status status; // ACTIVE, REMOVED, PENDING
}

//Represents the relationship between a user and a home.

//This is a many-to-many relationship mapping table between User and Home,
// with additional info like the user's role in that home and their status.