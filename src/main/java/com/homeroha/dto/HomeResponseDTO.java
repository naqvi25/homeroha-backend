package com.homeroha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponseDTO {
    private Long id;
    private String name;
    private String address;
    private Role role;
//    private List<HomeMemberDTO> members;
}
