package com.homeroha.dto;

// import com.homeroha.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeMemberDTO {
    private String email;
    private Role role;
}