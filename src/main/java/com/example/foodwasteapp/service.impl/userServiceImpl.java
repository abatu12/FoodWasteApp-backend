package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.user;
import com.example.foodwasteapp.dto.userDto;
import com.example.foodwasteapp.repository.userRepository;
import com.example.foodwasteapp.service.userService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class userServiceImpl implements userService {

    private final userRepository userRepo;

    public userServiceImpl(userRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<userDto> getAll() {
        return userRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public userDto getById(Long id) {
        user u = userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return toDto(u);
    }

    @Override
    public userDto create(userDto dto) {
        user entity = toEntity(dto);
        user saved = userRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public userDto update(Long id, userDto dto) {
        user existing = userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        existing.setUsername(dto.getUsername());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());
        existing.setLocation(dto.getLocation());
        user updated = userRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    private userDto toDto(user u) {
        userDto dto = new userDto();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setLocation(u.getLocation());
        return dto;
    }

    private user toEntity(userDto dto) {
        user u = new user();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setRole(dto.getRole());
        u.setLocation(dto.getLocation());
        return u;
    }
}
