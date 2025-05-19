package com.homeroha.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role; // ADMIN, MEMBER, GUEST
}
