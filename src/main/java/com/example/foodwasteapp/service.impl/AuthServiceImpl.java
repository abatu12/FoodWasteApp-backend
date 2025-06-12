package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.RefreshToken;
import com.example.foodwasteapp.dbmodel.User;
import com.example.foodwasteapp.dto.*;
import com.example.foodwasteapp.repository.RefreshTokenRepository;
import com.example.foodwasteapp.repository.UserRepository;
import com.example.foodwasteapp.service.AuthService;
import com.example.foodwasteapp.service.impl.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final JwtService jwt;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<AuthResponseDto> login(LoginRequestDto req, HttpServletResponse res) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        User u = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String access  = jwt.generateAccessToken(u.getUsername(), u.getRole());
        String refresh = jwt.generateRefreshToken(u.getUsername());
        RefreshToken rt = new RefreshToken();
        rt.setToken(refresh);
        rt.setUser(u);
        rt.setExpiryDate(Instant.now().plusMillis(JwtService.REFRESH_MS));
        refreshRepo.deleteByUser(u);
        refreshRepo.save(rt);

        Cookie cookie = new Cookie("accessToken", access);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int)(JwtService.ACCESS_MS / 1000));
        res.addCookie(cookie);

        AuthResponseDto body = AuthResponseDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .role(u.getRole())
                .build();
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<AuthResponseDto> refreshToken(String token, HttpServletResponse res) {
        RefreshToken rt = refreshRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (rt.getExpiryDate().isBefore(Instant.now())) {
            refreshRepo.delete(rt);
            throw new RuntimeException("Refresh token expired");
        }

        String username = jwt.extractUsername(token);
        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccess = jwt.generateAccessToken(username, u.getRole());

        Cookie cookie = new Cookie("accessToken", newAccess);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int)(JwtService.ACCESS_MS / 1000));
        res.addCookie(cookie);

        AuthResponseDto body = AuthResponseDto.builder()
                .accessToken(newAccess)
                .refreshToken(token)
                .role(u.getRole())
                .build();
        return ResponseEntity.ok(body);
    }

    @Override
    public void logout(String token) {
        refreshRepo.findByToken(token).ifPresent(refreshRepo::delete);
    }

    @Override
    public ResponseEntity<?> register(RegistrationRequestDto req) {
        if (userRepo.existsByUsername(req.getUsername()) ||
                userRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username or email already in use");
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole("USER");
        u.setEnabled(true);
        userRepo.save(u);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(u);
    }
}
