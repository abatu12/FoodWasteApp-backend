package com.example.foodwasteapp.service;

import com.example.foodwasteapp.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<AuthResponseDto> login(LoginRequestDto req, HttpServletResponse res);
    ResponseEntity<AuthResponseDto> refreshToken(String token, HttpServletResponse res);
    void logout(String token);

    ResponseEntity<?> register(RegistrationRequestDto req);
}
