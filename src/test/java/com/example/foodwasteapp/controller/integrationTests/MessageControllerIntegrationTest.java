package com.example.foodwasteapp.controller.integrationTests;

import com.example.foodwasteapp.controller.MessageController;
import com.example.foodwasteapp.dto.MessageDto;
import com.example.foodwasteapp.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})

public class MessageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MessageDto sampleMessageDto;

    @BeforeEach
    public void setup() {
        messageRepository.deleteAll();

        sampleMessageDto = new MessageDto();
        sampleMessageDto.setSenderID(1L);
        sampleMessageDto.setReceiverID(2L);
        sampleMessageDto.setListingID(3L);
        sampleMessageDto.setText("Hello from test");
        sampleMessageDto.setTimestamp(LocalDateTime.now());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateMessage() throws Exception {
        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleMessageDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senderID", is(sampleMessageDto.getSenderID().intValue())))
                .andExpect(jsonPath("$.text", is(sampleMessageDto.getText())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllMessages() throws Exception {
        // Create a message first
        String content = objectMapper.writeValueAsString(sampleMessageDto);
        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetMessageById() throws Exception {
        // Create message and get its ID
        String json = mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleMessageDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MessageDto created = objectMapper.readValue(json, MessageDto.class);

        mockMvc.perform(get("/messages/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(created.getId().intValue())))
                .andExpect(jsonPath("$.text", is(created.getText())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateMessage() throws Exception {
        // Create message and get ID
        String json = mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleMessageDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MessageDto created = objectMapper.readValue(json, MessageDto.class);

        // Modify text
        created.setText("Updated text");

        mockMvc.perform(put("/messages/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("Updated text")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")  // Pretpostavimo da brisanje zahtijeva ADMIN ulogu
    public void testDeleteMessage() throws Exception {
        // Create message and get ID
        String json = mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleMessageDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MessageDto created = objectMapper.readValue(json, MessageDto.class);

        mockMvc.perform(delete("/messages/{id}", created.getId()))
                .andExpect(status().isOk());

        // Provjeri da je obrisano
        mockMvc.perform(get("/messages/{id}", created.getId()))
                .andExpect(status().isNotFound());
    }

}
