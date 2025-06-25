package com.example.foodwasteapp.controller;

import com.example.foodwasteapp.dbmodel.User;
import com.example.foodwasteapp.dto.RecipeDto;
import com.example.foodwasteapp.service.RecipeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDto> getAllRecipes() {
        return recipeService.getAll();
    }

    @GetMapping("/{id}")
    public RecipeDto getRecipeById(@PathVariable Long id) {
        return recipeService.getById(id);
    }

    @PostMapping
    public RecipeDto createRecipe(@RequestBody RecipeDto dto) {
        return recipeService.create(dto);
    }

    @PutMapping("/{id}")
    public RecipeDto updateRecipe(
            @PathVariable Long id,
            @RequestBody RecipeDto dto
    ) {
        return recipeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.delete(id);
    }
}
