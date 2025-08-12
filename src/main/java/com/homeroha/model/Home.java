package com.homeroha.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "homes")
@Data // Generates getters, setters, toString(), equals(), and hashCode() automatically.
@NoArgsConstructor
@AllArgsConstructor
@Builder // Enables you to create Home objects using the builder pattern.
@EntityListeners(AuditingEntityListener.class) // Hooks into Spring Data JPA’s auditing feature.
//Makes @CreatedDate (below) work automatically without you needing to set createdAt manually.

public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    @CreatedDate
    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL)
//    private Set<UserHome> userHomes;
}
