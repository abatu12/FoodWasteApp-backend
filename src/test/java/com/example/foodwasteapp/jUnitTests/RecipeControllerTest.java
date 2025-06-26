package com.example.foodwasteapp.jUnitTests;



import com.example.foodwasteapp.dbmodel.Recipe;
import com.example.foodwasteapp.dto.RecipeDto;
import com.example.foodwasteapp.repository.RecipeRepository;
import com.example.foodwasteapp.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

        import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
        import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private Recipe recipe;
    private RecipeDto recipeDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setUserID(1L);
        recipe.setTitle("Test Recipe");
        recipe.setDescription("Test Desc");
        recipe.setIngredients("Test Ingredients");
        recipe.setSteps("Test Steps");
        recipe.setImage("image.jpg");
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        recipe.setEnabled("true");

        recipeDto = new RecipeDto();
        recipeDto.setUserID(1L);
        recipeDto.setTitle("Test Recipe");
        recipeDto.setDescription("Test Desc");
        recipeDto.setIngredients("Test Ingredients");
        recipeDto.setSteps("Test Steps");
        recipeDto.setImage("image.jpg");
        recipeDto.setCreatedAt(LocalDateTime.now());
        recipeDto.setUpdatedAt(LocalDateTime.now());
        recipeDto.setEnabled(true);
    }

    @Test
    public void testGetAll() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        List<RecipeDto> result = recipeService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Recipe");
        verify(recipeRepository).findAll();
    }

    @Test
    public void testGetById_found() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        RecipeDto dto = recipeService.getById(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Test Recipe");
        verify(recipeRepository).findById(1L);
    }

    @Test
    public void testGetById_notFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeService.getById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Recipe not found");
    }

    @Test
    public void testCreate() {
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        RecipeDto created = recipeService.create(recipeDto);

        assertThat(created.getTitle()).isEqualTo("Test Recipe");
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    public void testUpdate_found() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        RecipeDto updatedDto = new RecipeDto();
        updatedDto.setUserID(2L);
        updatedDto.setTitle("Updated Title");
        updatedDto.setDescription("Updated Desc");
        updatedDto.setIngredients("Updated Ingredients");
        updatedDto.setSteps("Updated Steps");
        updatedDto.setImage("updated-image.jpg");
        updatedDto.setCreatedAt(LocalDateTime.now());
        updatedDto.setUpdatedAt(LocalDateTime.now());
        updatedDto.setEnabled(false);

        RecipeDto result = recipeService.update(1L, updatedDto);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.isEnabled()).isFalse();

        verify(recipeRepository).findById(1L);
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    public void testUpdate_notFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeService.update(1L, recipeDto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Recipe not found");
    }

    @Test
    public void testDelete() {
        doNothing().when(recipeRepository).deleteById(1L);

        recipeService.delete(1L);

        verify(recipeRepository).deleteById(1L);
    }
}
