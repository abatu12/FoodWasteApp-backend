package com.example.foodwasteapp.controller.integrationTests;


import com.example.foodwasteapp.dto.RecipeDto;
import com.example.foodwasteapp.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.NoSuchElementException;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private RecipeDto sampleRecipeDto;

    @BeforeEach
    public void setup() {
        recipeRepository.deleteAll();

        sampleRecipeDto = new RecipeDto();
        sampleRecipeDto.setUserID(1L);
        sampleRecipeDto.setTitle("Test Recipe");
        sampleRecipeDto.setDescription("Test Description");
        sampleRecipeDto.setIngredients("Test Ingredients");
        sampleRecipeDto.setSteps("Test Steps");
        sampleRecipeDto.setImage("test-image.jpg");
        sampleRecipeDto.setCreatedAt(LocalDateTime.now());
        sampleRecipeDto.setUpdatedAt(LocalDateTime.now());
        sampleRecipeDto.setEnabled(true);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateRecipe() throws Exception {
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRecipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(sampleRecipeDto.getTitle())))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllRecipes() throws Exception {
        // prvo dodaj recept u bazu
        String content = objectMapper.writeValueAsString(sampleRecipeDto);
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    @WithMockUser(roles = "USER")
    public void testGetRecipeById() throws Exception {
        // kreiraj recept i dohvati id
        String json = mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRecipeDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RecipeDto created = objectMapper.readValue(json, RecipeDto.class);

        mockMvc.perform(get("/recipes/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(created.getId().intValue())))
                .andExpect(jsonPath("$.title", is(created.getTitle())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateRecipe() throws Exception {
        // kreiraj recept
        String json = mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRecipeDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RecipeDto created = objectMapper.readValue(json, RecipeDto.class);

        created.setTitle("Updated Title");
        created.setEnabled(false);

        mockMvc.perform(put("/recipes/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.enabled", is(false)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteRecipe() throws Exception {
        // 1. Prvo kreiraj recept u bazi
        String json = mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRecipeDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RecipeDto created = objectMapper.readValue(json, RecipeDto.class);
        Long recipeId = created.getId();

        // 2. Provjeri da recept postoji
        mockMvc.perform(get("/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(recipeId.intValue())));

        // 3. Obriši recept
        mockMvc.perform(delete("/recipes/{id}", recipeId))
                .andExpect(status().isOk());

        // 4. Provjeri da je recept stvarno obrisan - sada će vratiti 404 zbog GlobalExceptionHandler-a
        mockMvc.perform(get("/recipes/{id}", recipeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteNonExistentRecipe() throws Exception {
        // Test brisanja recepta koji ne postoji
        Long nonExistentId = 999L;

        mockMvc.perform(delete("/recipes/{id}", nonExistentId))
                .andExpect(status().isOk()); // DELETE operacije često vraćaju 200 OK čak i ako entitet ne postoji

        // Alternativno, ako vaša implementacija baca iznimku:
        // .andExpect(status().isNotFound());
    }

}

