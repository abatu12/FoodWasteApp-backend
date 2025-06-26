package com.example.foodwasteapp.controller.integrationTests;

import com.example.foodwasteapp.dbmodel.Listing;
import com.example.foodwasteapp.dto.ListingDto;
import com.example.foodwasteapp.dto.LoginRequestDto;
import com.example.foodwasteapp.dto.RegistrationRequestDto;
import com.example.foodwasteapp.repository.ListingRepository;
import com.example.foodwasteapp.repository.RefreshTokenRepository;
import com.example.foodwasteapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
//@Transactional

//@Import(ListingControllerIntegrationTest.TestExceptionHandler.class)


class listingcontrollerintegrationtest {
    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private Long listingId;

    @BeforeEach
    public void setup() throws Exception {
        // obriši sve iz repozitorija (pazi redoslijed ako ima FK)
        refreshTokenRepository.deleteAll();
        listingRepository.deleteAll();
        userRepository.deleteAll();

        // registriraj i autentificiraj korisnika
        registerAndAuthenticateUser();

        // kreiraj novi listing u bazi
        Listing listing = new Listing();
        listing.setTitle("Test Listing");
        listing.setDescription("Opis za test");
        // postavi ostala polja ako treba

        Listing saved = listingRepository.save(listing);
        listingId = saved.getId();
    }


    private String accessToken;
    private Long userId;



    @AfterEach
    public void cleanup() {
        refreshTokenRepository.deleteAll();
        listingRepository.deleteAll();
        userRepository.deleteAll();
    }

    private void registerAndAuthenticateUser() throws Exception {
        RegistrationRequestDto registrationRequest = new RegistrationRequestDto();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("testuser@example.com");
        registrationRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        accessToken = objectMapper.readTree(loginResponse).get("accessToken").asText();
        userId = userRepository.findByUsername("testuser").get().getId();
    }

    // 1. CREATE
    @Test
    public void testCreateListing() throws Exception {
        ListingDto listingDto = new ListingDto();
        listingDto.setTitle("Test Listing");
        listingDto.setDescription("Test Description");
        listingDto.setQuantity(10);
        listingDto.setExpiryDate(LocalDate.now().plusDays(7));
        listingDto.setCreatedAt(LocalDateTime.now());
        listingDto.setImage("image.jpg");
        listingDto.setUserID(userId);

        mockMvc.perform(post("/listings")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Listing"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    // 2. READ
    @Test
    public void testGetListingById() throws Exception {
        ListingDto listingDto = new ListingDto();
        listingDto.setTitle("Test Listing");
        listingDto.setDescription("Test Description");
        listingDto.setQuantity(10);
        listingDto.setExpiryDate(LocalDate.now().plusDays(7));
        listingDto.setCreatedAt(LocalDateTime.now());
        listingDto.setImage("image.jpg");
        listingDto.setUserID(userId);

        String response = mockMvc.perform(post("/listings")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long createdId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/listings/" + createdId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.title").value("Test Listing"));
    }

    // 3. UPDATE
    @Test
    public void testUpdateListing() throws Exception {
        ListingDto listingDto = new ListingDto();
        listingDto.setTitle("Original Title");
        listingDto.setDescription("Original Description");
        listingDto.setQuantity(5);
        listingDto.setExpiryDate(LocalDate.now().plusDays(5));
        listingDto.setCreatedAt(LocalDateTime.now());
        listingDto.setImage("orig.jpg");
        listingDto.setUserID(userId);

        String response = mockMvc.perform(post("/listings")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long createdId = objectMapper.readTree(response).get("id").asLong();

        ListingDto updatedDto = new ListingDto();
        updatedDto.setTitle("Updated Title");
        updatedDto.setDescription("Updated Description");
        updatedDto.setQuantity(15);
        updatedDto.setExpiryDate(LocalDate.now().plusDays(10));
        updatedDto.setCreatedAt(LocalDateTime.now());
        updatedDto.setImage("updated.jpg");
        updatedDto.setUserID(userId);

        mockMvc.perform(put("/listings/" + createdId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    public void testGetAllListings() throws Exception {
        listingRepository.deleteAll(); // <--- počisti sve prethodne podatke

        ListingDto listingDto = new ListingDto();
        listingDto.setTitle("Test All");
        listingDto.setDescription("Opis All");
        listingDto.setQuantity(3);
        listingDto.setExpiryDate(LocalDate.now().plusDays(3));
        listingDto.setCreatedAt(LocalDateTime.now());
        listingDto.setImage("all.jpg");
        listingDto.setUserID(userId);

        mockMvc.perform(post("/listings")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listingDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/listings")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test All"));
    }

    // 4. DELETE
    @Test
    public void testDeleteListing_thenGetReturns404() throws Exception {
        // DELETE treba vratiti 204 No Content
        mockMvc.perform(delete("/listings/{id}", listingId)
                        .header("Authorization", "Bearer " + accessToken)  // dodaj token
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // GET nakon brisanja treba vratiti 404 Not Found
        mockMvc.perform(get("/listings/{id}", listingId)
                        .header("Authorization", "Bearer " + accessToken)  // dodaj token
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}



