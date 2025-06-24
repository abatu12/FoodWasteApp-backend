package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.UserDto;
import java.util.List;

public interface UserService {
    List<UserDto> getAll();
    UserDto getById(Long id);
    UserDto create(UserDto dto);
    UserDto update(Long id, UserDto dto);
    void delete(Long id);
}
