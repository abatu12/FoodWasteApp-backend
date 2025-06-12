package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.*;
import com.example.foodwasteapp.dto.*;
import com.example.foodwasteapp.repository.*;
import com.example.foodwasteapp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final JwtService jwt;
    private final PasswordEncoder encoder;

    @Override
    public ResponseEntity<AuthResponseDto> login(LoginRequestDto req, HttpServletResponse res) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        User u = userRepo.findByUsername(req.getUsername())
                .orElseThrow();
        String access = jwt.generateAccessToken(u.getUsername(), u.getRole());
        String refresh = jwt.generateRefreshToken(u.getUsername());

        RefreshToken rt = new RefreshToken();
        rt.setToken(refresh);
        rt.setUser(u);
        rt.setExpiryDate(Instant.now().plusMillis(jwt.REFRESH_MS));
        refreshRepo.save(rt);

        Cookie c = new Cookie("accessToken", access);
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge((int)(jwt.ACCESS_MS/1000));
        res.addCookie(c);

        return ResponseEntity.ok(AuthResponseDto.builder()
                .refreshToken(refresh)
                .role(u.getRole())
                .build());
    }

    @Override
    public ResponseEntity<AuthResponseDto> refreshToken(String token, HttpServletResponse res) {
        RefreshToken rt = refreshRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (rt.getExpiryDate().isBefore(Instant.now())) {
            refreshRepo.delete(rt);
            throw new RuntimeException("Expired");
        }
        String user = jwt.getUsername(token);
        User u = userRepo.findByUsername(user).get();
        String newAccess = jwt.generateAccessToken(user, u.getRole());

        Cookie c = new Cookie("accessToken", newAccess);
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge((int)(jwt.ACCESS_MS/1000));
        res.addCookie(c);

        return ResponseEntity.ok(AuthResponseDto.builder()
                .refreshToken(token)
                .role(u.getRole())
                .build());
    }

    @Override
    public void logout(String token) {
        refreshRepo.findByToken(token).ifPresent(refreshRepo::delete);
    }
}
