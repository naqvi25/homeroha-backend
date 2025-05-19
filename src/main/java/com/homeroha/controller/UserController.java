package com.homeroha.controller;

import com.homeroha.dto.AuthResponse;
import com.homeroha.dto.UserDashboardDTO;
import com.homeroha.model.Home;
import com.homeroha.model.User;
import com.homeroha.repository.UserRepository;
import com.homeroha.security.JwtTokenProvider;
import com.homeroha.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

//        Home home = null;


        AuthResponse response = AuthResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .token(null) // don't send token again
                .build();

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/me")
//    public ResponseEntity<UserDashboardDTO> getUserDashboard(Principal principal) {
//        return ResponseEntity.ok(userService.getDashboardInfo(principal.getName()));
//    }
}