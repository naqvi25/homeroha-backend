package com.homeroha.dto;

import lombok.Data;

@Data
public class InvitationRequestDTO {
    private String email;     // Invitee's email
    private Long homeId;      // Home to which they are being invited
    private Role role;        // Role to assign: ADMIN, MEMBER, GUEST
    private String homeName; // custom field from related Home
}
