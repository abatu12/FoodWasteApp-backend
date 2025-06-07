package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.recipeDto;
import java.util.List;

public interface recipeService {
    List<recipeDto> getAll();
    recipeDto getById(Long id);
    recipeDto create(recipeDto dto);
    recipeDto update(Long id, recipeDto dto);
    void delete(Long id);
}
