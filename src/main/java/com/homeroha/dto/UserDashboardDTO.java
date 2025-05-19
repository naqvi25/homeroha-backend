package com.homeroha.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDashboardDTO {
    private String name;
    private String email;
    private Role role;
    private HomeDTO activeHome;

    @Data
    @Builder
    public static class HomeDTO {
        private Long id;
        private String name;
        private String address;
    }
}
