package com.homeroha.service.impl;

import com.homeroha.dto.*;
import com.homeroha.exception.HomerohaException;
import com.homeroha.model.Home;
import com.homeroha.model.User;
import com.homeroha.model.UserHome;
import com.homeroha.repository.HomeRepository;
import com.homeroha.repository.UserHomeRepository;
import com.homeroha.repository.UserRepository;
import com.homeroha.service.HomeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;
    private final UserRepository userRepository;
    private final UserHomeRepository userHomeRepository;

    @Override
    public HomeResponseDTO createHome(HomeRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Home home = Home.builder()
                .name(request.getName())
                .address(request.getAddress())
                .createdAt(LocalDateTime.now())
                .build();

        homeRepository.save(home);

        UserHome userHome = UserHome.builder()
                .user(user)
                .home(home)
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .build();

        userHomeRepository.save(userHome);

        return HomeResponseDTO.builder()
                .id(home.getId())
                .name(home.getName())
                .address(home.getAddress())
                .build();
    }

    @Override
    public void setActiveHomeForUser(String email, Long homeId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));

        boolean isMember = userHomeRepository.existsByUserAndHome(user, home);
        if (!isMember) {
            throw new RuntimeException("User is not a member of this home");
        }

        user.setActiveHome(home);
        userRepository.save(user);
    }

    @Override
    public List<HomeResponseDTO> getHomesForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserHome> userHomes = userHomeRepository.findAllByUser(user);

        return userHomes.stream()
                .map(uh -> {
                    Home home = uh.getHome();
                    return new HomeResponseDTO(home.getId(), home.getName(), home.getAddress(), uh.getRole());
                })
                .collect(Collectors.toList());
    }

    @Override
    public HomeDetailsResponseDTO getHomeDetails(String userEmail, Long homeId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new HomerohaException("Home not found"));

        boolean isMember = userHomeRepository.existsByUserAndHome(user, home);
        if (!isMember) {
            throw new HomerohaException("You are not a member of this home");
        }

        List<HomeMemberDTO> members = userHomeRepository.findAllByHome(home).stream()
                .map(uh -> new HomeMemberDTO(uh.getUser().getEmail(), uh.getRole()))
                .toList();

        return new HomeDetailsResponseDTO(home.getId(), home.getName(), home.getAddress(), members);
    }

    @Override
    public HomeResponseDTO getActiveHomeForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Home activeHome = user.getActiveHome();
        if (activeHome == null) {
            throw new RuntimeException("No active home set for user");
        }

        // ✅ Membership check
        boolean isHomeMember = userHomeRepository.existsByUserAndHome(user, activeHome);
        if (!isHomeMember) {
            throw new RuntimeException("User is not a member of the active home");
        }

        return HomeResponseDTO.builder()
                .id(activeHome.getId())
                .name(activeHome.getName())
                .address(activeHome.getAddress())
                .build();
    }

    @Override
    public void deleteHome(String userEmail, Long homeId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new HomerohaException("Home not found"));

        UserHome userHome = userHomeRepository.findByUserAndHome(user, home)
                .orElseThrow(() -> new HomerohaException("You are not part of this home"));

        if (userHome.getRole() != Role.ADMIN) {
            throw new HomerohaException("You are not authorized to delete this home");
        }

        userHomeRepository.deleteAll(userHomeRepository.findAllByHome(home));
        homeRepository.delete(home);
    }
}