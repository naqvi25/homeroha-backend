package com.homeroha.service.impl;

import com.homeroha.dto.UserDashboardDTO;
import com.homeroha.exception.HomerohaException;
import com.homeroha.model.Home;
import com.homeroha.model.User;
import com.homeroha.repository.UserRepository;
import com.homeroha.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDashboardDTO getDashboardInfo(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        UserDashboardDTO.UserDashboardDTOBuilder builder = UserDashboardDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole());

        if (user.getActiveHome() != null) {
            Home home = user.getActiveHome();
            builder.activeHome(UserDashboardDTO.HomeDTO.builder()
                    .id(home.getId())
                    .name(home.getName())
                    .address(home.getAddress())
                    .build());
        }

        return builder.build();
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}
