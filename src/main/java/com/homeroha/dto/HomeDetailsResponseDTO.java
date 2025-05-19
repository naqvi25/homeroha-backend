package com.homeroha.dto;

import com.homeroha.dto.HomeMemberDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HomeDetailsResponseDTO {
    private Long id;
    private String name;
    private String address;
    private List<HomeMemberDTO> members;
}