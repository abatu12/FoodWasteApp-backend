package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.recipe;
import com.example.foodwasteapp.dto.recipeDto;
import com.example.foodwasteapp.repository.recipeRepository;
import com.example.foodwasteapp.service.recipeService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class recipeServiceImpl implements recipeService {

    private final recipeRepository recipeRepo;

    public recipeServiceImpl(recipeRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }

    @Override
    public List<recipeDto> getAll() {
        return recipeRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public recipeDto getById(Long id) {
        recipe r = recipeRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recipe not found"));
        return toDto(r);
    }

    @Override
    public recipeDto create(recipeDto dto) {
        recipe entity = toEntity(dto);
        recipe saved = recipeRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public recipeDto update(Long id, recipeDto dto) {
        recipe existing = recipeRepo.findById(id)
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
        recipe updated = recipeRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        recipeRepo.deleteById(id);
    }
    private recipeDto toDto(recipe r) {
        recipeDto dto = new recipeDto();
        dto.setId(r.getId());
        dto.setUserID(r.getUserID());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());
        dto.setIngredients(r.getIngredients());
        dto.setSteps(r.getSteps());
        dto.setImage(r.getImage());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        dto.setEnabled(r.getEnabled().equals("true"));
        return dto;
    }

    private recipe toEntity(recipeDto dto) {
        recipe r = new recipe();
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
