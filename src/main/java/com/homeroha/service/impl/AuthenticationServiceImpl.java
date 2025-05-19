package com.homeroha.service.impl;

import com.homeroha.dto.*;
import com.homeroha.model.User;
import com.homeroha.repository.UserRepository;
import com.homeroha.security.JwtTokenProvider;
import com.homeroha.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
import com.homeroha.dto.AuthRequest;
import com.homeroha.dto.AuthResponse;
import com.homeroha.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

//    @Value("${google.client-id}")
    @Value("${google.client-id:}")
    private String googleClientId;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtTokenProvider.generateToken(user.getEmail());
            return AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

//    @Override
//    public AuthResponse loginWithGoogle(String idTokenString) {
//        try {
//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                    GoogleNetHttpTransport.newTrustedTransport(),
//                    JacksonFactory.getDefaultInstance()
//            )
//                    .setAudience(Collections.singletonList(googleClientId))
//                    .build();

//            GoogleIdToken idToken = verifier.verify(idTokenString);
//            if (idToken == null) {
//                throw new RuntimeException("Invalid Google ID token");
//            }

//            GoogleIdToken.Payload payload = idToken.getPayload();
//            String email = payload.getEmail();
//            String name  = (String) payload.get("name");

//            User user = userRepository.findByEmail(email)
//                    .orElseGet(() -> {
//                        User u = new User();
//                        u.setEmail(email);
//                        u.setName(name);
//                        u.setPassword("");           // no local password
//                        u.setRole(Role.MEMBER);      // default role
//                        return userRepository.save(u);
//                    });
//
//            String jwt = jwtTokenProvider.generateToken(user.getEmail());
//            return AuthResponse.builder()
//                    .token(jwt)
//                    .email(user.getEmail())
//                    .name(user.getName())
//                    .role(user.getRole())
//                    .build();

//        } catch (Exception e) {
//            throw new RuntimeException("Google sign-in failed: " + e.getMessage(), e);
//        }
//    }

}
