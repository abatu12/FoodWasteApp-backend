package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.User;
import com.example.foodwasteapp.dto.UserDto;
import com.example.foodwasteapp.repository.UserRepository;
import com.example.foodwasteapp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDto getById(Long id) {
        User u = userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return toDto(u);
    }

    @Override
    public UserDto create(UserDto dto) {
        User entity = toEntity(dto);
        User saved = userRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User existing = userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        existing.setUsername(dto.getUsername());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());
        existing.setLocation(dto.getLocation());
        User updated = userRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    private UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setLocation(u.getLocation());
        return dto;
    }

    private User toEntity(UserDto dto) {
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setRole(dto.getRole());
        u.setLocation(dto.getLocation());
        return u;
    }
}
