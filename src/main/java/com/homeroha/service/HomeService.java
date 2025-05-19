package com.homeroha.service;

import com.homeroha.dto.HomeDetailsResponseDTO;
import com.homeroha.dto.HomeRequestDTO;
import com.homeroha.dto.HomeResponseDTO;

import java.util.List;

public interface HomeService {
    HomeResponseDTO createHome(HomeRequestDTO request);
    void setActiveHomeForUser(String email, Long homeId); // <- Add this line
    List<HomeResponseDTO> getHomesForUser(String email);
    HomeDetailsResponseDTO getHomeDetails(String userEmail, Long homeId);
    public HomeResponseDTO getActiveHomeForUser(String email);
    void deleteHome(String userEmail, Long homeId);

}

