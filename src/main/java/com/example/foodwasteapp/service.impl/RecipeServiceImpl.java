package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.Recipe;
import com.example.foodwasteapp.dto.RecipeDto;
import com.example.foodwasteapp.repository.RecipeRepository;
import com.example.foodwasteapp.service.RecipeService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepo;

    public RecipeServiceImpl(RecipeRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }

    @Override
    public List<RecipeDto> getAll() {
        List<Recipe> recipes = recipeRepo.findAll();
        System.out.println(">>> Ukupno pronađenih recepata: " + recipes.size());
        System.out.println(">>> Ukupno pronađenih recepata: " + recipes.size());
        recipes.forEach(r -> System.out.println(">>> Recept: " + r.getTitle()));
        return recipes.stream()
                .map(this::toDto)
                .toList();
    }


    @Override
    public RecipeDto getById(Long id) {
        Recipe r = recipeRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recipe not found"));
        return toDto(r);
    }

    @Override
    public RecipeDto create(RecipeDto dto) {
        Recipe entity = toEntity(dto);
        Recipe saved = recipeRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public RecipeDto update(Long id, RecipeDto dto) {
        Recipe existing = recipeRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recipe not found"));
        existing.setUserID(dto.getUserID());
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setIngredients(dto.getIngredients());
        existing.setSteps(dto.getSteps());
        existing.setImage(dto.getImage());
        existing.setCreatedAt(dto.getCreatedAt());
        existing.setUpdatedAt(dto.getUpdatedAt());
        existing.setEnabled(dto.isEnabled() ? "true" : "false");
        Recipe updated = recipeRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        recipeRepo.deleteById(id);
    }
    private RecipeDto toDto(Recipe r) {
        RecipeDto dto = new RecipeDto();
        dto.setId(r.getId());
        dto.setUserID(r.getUserID());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());
        dto.setIngredients(r.getIngredients());
        dto.setSteps(r.getSteps());
        dto.setImage(r.getImage());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        dto.setEnabled(Boolean.parseBoolean(r.getEnabled()));
        return dto;
    }

    private Recipe toEntity(RecipeDto dto) {
        Recipe r = new Recipe();
        r.setUserID(dto.getUserID());
        r.setTitle(dto.getTitle());
        r.setDescription(dto.getDescription());
        r.setIngredients(dto.getIngredients());
        r.setSteps(dto.getSteps());
        r.setImage(dto.getImage());
        r.setCreatedAt(dto.getCreatedAt());
        r.setUpdatedAt(dto.getUpdatedAt());
        r.setEnabled(dto.isEnabled() ? "true" : "false");
        return r;
    }
}
