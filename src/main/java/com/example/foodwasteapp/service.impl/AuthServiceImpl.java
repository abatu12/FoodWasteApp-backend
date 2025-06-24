package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.RefreshToken;
import com.example.foodwasteapp.dbmodel.User;
import com.example.foodwasteapp.dto.LoginRequestDto;
import com.example.foodwasteapp.dto.AuthResponseDto;
import com.example.foodwasteapp.dto.RegistrationRequestDto;
import com.example.foodwasteapp.repository.RefreshTokenRepository;
import com.example.foodwasteapp.repository.UserRepository;
import com.example.foodwasteapp.service.AuthService;
import com.example.foodwasteapp.service.impl.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final JwtService jwt;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource msg;

    @Override
    public ResponseEntity<AuthResponseDto> login(LoginRequestDto req, HttpServletResponse res) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        } catch (BadCredentialsException ex) {
            String error = msg.getMessage("auth.login.badcredentials", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponseDto.builder().message(error).build());
        }
        User u = userRepo.findByUsername(req.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String access = jwt.generateAccessToken(u.getUsername(), u.getRole());
        String refresh = jwt.generateRefreshToken(u.getUsername());
        refreshRepo.deleteByUser(u);
        RefreshToken rt = new RefreshToken();
        rt.setToken(refresh);
        rt.setUser(u);
        rt.setExpiryDate(Instant.now().plusMillis(JwtService.REFRESH_MS));
        refreshRepo.save(rt);
        Cookie c = new Cookie("accessToken", access);
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge((int)(JwtService.ACCESS_MS/1000));
        res.addCookie(c);
        String success = msg.getMessage("auth.login.success", null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(AuthResponseDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .role(u.getRole())
                .message(success)
                .build());
    }

    @Override
    public ResponseEntity<AuthResponseDto> refreshToken(String token, HttpServletResponse res) {
        RefreshToken rt = refreshRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException(msg.getMessage("auth.refresh.invalid", null, LocaleContextHolder.getLocale())));
        if (rt.getExpiryDate().isBefore(Instant.now())) {
            refreshRepo.delete(rt);
            throw new RuntimeException(msg.getMessage("auth.refresh.expired", null, LocaleContextHolder.getLocale()));
        }
        String username = jwt.extractUsername(token);
        User u = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String newAccess = jwt.generateAccessToken(username, u.getRole());
        Cookie c = new Cookie("accessToken", newAccess);
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge((int)(JwtService.ACCESS_MS/1000));
        res.addCookie(c);
        String refreshed = msg.getMessage("auth.refresh.success", null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(AuthResponseDto.builder()
                .accessToken(newAccess)
                .refreshToken(token)
                .role(u.getRole())
                .message(refreshed)
                .build());
    }

    @Override
    public void logout(String token) {
        refreshRepo.findByToken(token).ifPresent(refreshRepo::delete);
    }

    @Override
    public ResponseEntity<AuthResponseDto> register(RegistrationRequestDto req) {
        if (userRepo.existsByUsername(req.getUsername()) || userRepo.existsByEmail(req.getEmail())) {
            String err = msg.getMessage("auth.register.conflict", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthResponseDto.builder().message(err).build());
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole("USER");
        userRepo.save(u);
        String access = jwt.generateAccessToken(u.getUsername(), u.getRole());
        String refresh = jwt.generateRefreshToken(u.getUsername());
        refreshRepo.deleteByUser(u);
        RefreshToken rt = new RefreshToken();
        rt.setToken(refresh);
        rt.setUser(u);
        rt.setExpiryDate(Instant.now().plusMillis(JwtService.REFRESH_MS));
        refreshRepo.save(rt);
        String ok = msg.getMessage("auth.register.success", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponseDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .role(u.getRole())
                .message(ok)
                .build());
    }
}
