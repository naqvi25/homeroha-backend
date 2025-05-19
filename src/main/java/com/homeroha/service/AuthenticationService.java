package com.homeroha.service;

import com.homeroha.dto.AuthRequest;
import com.homeroha.dto.AuthResponse;
import com.homeroha.dto.RegisterRequest;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
//    AuthResponse loginWithGoogle(String idToken);
}
