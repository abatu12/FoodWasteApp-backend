package com.example.foodwasteapp.controller.integrationTests;

import com.example.foodwasteapp.dto.LoginRequestDto;
import com.example.foodwasteapp.dto.RegistrationRequestDto;
import com.example.foodwasteapp.repository.RefreshTokenRepository;
import com.example.foodwasteapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setup() {
        // Očisti bazu prije svakog testa (prvo refresh tokeni zbog FK ograničenja)
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void registerLoginAndRefresh_successFlow() throws Exception {
        // 1. Registracija
        RegistrationRequestDto registrationRequest = new RegistrationRequestDto();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("testuser@example.com");
        registrationRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        // 2. Login
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Pretpostavimo da ti login vraća JSON s tokenom, npr:
        // { "accessToken": "...", "refreshToken": "..." }
        // Izvuci refreshToken iz odgovora:
        String refreshToken = objectMapper.readTree(loginResponse).get("refreshToken").asText();

        // 3. Refresh token
        String refreshJson = "{\"refreshToken\":\"" + refreshToken + "\"}";

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshJson))
                .andExpect(status().isOk());
    }
}
