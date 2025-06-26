package com.example.foodwasteapp.controller.integrationTests;

import com.example.foodwasteapp.dto.AuthResponseDto;
import com.example.foodwasteapp.dto.ClaimDto;
import com.example.foodwasteapp.dto.LoginRequestDto;
import com.example.foodwasteapp.dto.RegistrationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClaimControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeAll
    void setupUserAndAuthenticate() throws Exception {
        // 1. Registracija korisnika preko REST API-ja
        RegistrationRequestDto registrationRequest = new RegistrationRequestDto();
        registrationRequest.setUsername("admin");
        registrationRequest.setEmail("admin@example.com");
        registrationRequest.setPassword("admin");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        // 2. Login da dobijemo JWT token
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername("admin");
        loginDto.setPassword("admin");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponseDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), AuthResponseDto.class);

        jwtToken = response.getAccessToken();
    }

    @Test
    void testGetAllClaims_withValidToken_shouldReturn200() throws Exception {
        mockMvc.perform(get("/claims")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateClaim_withValidToken_shouldReturnCreatedClaim() throws Exception {
        ClaimDto claimDto = new ClaimDto();
        claimDto.setRequesterID(1L);
        claimDto.setListingID(2L);
        claimDto.setStatus("PENDING");
        claimDto.setRequestedAt(LocalDateTime.now());

        mockMvc.perform(post("/claims")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claimDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testDeleteClaim_withValidToken_shouldReturn200() throws Exception {
        // 1. Kreiraj novi Claim
        ClaimDto claimDto = new ClaimDto();
        claimDto.setRequesterID(1L);
        claimDto.setListingID(2L);
        claimDto.setStatus("PENDING");
        claimDto.setRequestedAt(LocalDateTime.now());

        MvcResult createResult = mockMvc.perform(post("/claims")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claimDto)))
                .andExpect(status().isOk())
                .andReturn();

        ClaimDto createdClaim = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), ClaimDto.class);

        // 2. Obriši Claim
        mockMvc.perform(delete("/claims/{id}", createdClaim.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        // 3. (Opcionalno) Provjeri da više ne postoji
        mockMvc.perform(get("/claims/{id}", createdClaim.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

}
