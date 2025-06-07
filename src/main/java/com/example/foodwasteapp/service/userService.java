package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.userDto;
import java.util.List;

public interface userService{
    List<userDto> getAll();
    userDto getById(Long id);
    userDto create(userDto dto);
    userDto update(Long id, userDto dto);
    void delete(Long id);
}
