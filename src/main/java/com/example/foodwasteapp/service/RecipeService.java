package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.RecipeDto;
import java.util.List;

public interface RecipeService {
    List<RecipeDto> getAll();
    RecipeDto getById(Long id);
    RecipeDto create(RecipeDto dto);
    RecipeDto update(Long id, RecipeDto dto);
    void delete(Long id);
}
