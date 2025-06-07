package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface recipeRepository extends JpaRepository<recipe, Long> {
}
