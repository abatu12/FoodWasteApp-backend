package com.example.foodwasteapp.controller;

import com.example.foodwasteapp.dto.*;
import com.example.foodwasteapp.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto req,
            HttpServletResponse res
    ) {
        return authService.login(req, res);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @Valid @RequestBody RefreshTokenRequestDto req,
            HttpServletResponse res
    ) {
        return authService.refreshToken(req.getRefreshToken(), res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequestDto req
    ) {
        authService.logout(req.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
