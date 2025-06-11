package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
