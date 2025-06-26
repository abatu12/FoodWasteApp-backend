/*package com.example.foodwasteapp.controller.integrationTests;

import com.example.foodwasteapp.dbmodel.User;
import com.example.foodwasteapp.dto.UserDto;
import com.example.foodwasteapp.repository.UserRepository;
import com.example.foodwasteapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsersTests {
}

/*import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;*/

/*@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public List<UserDto> getAll() {
        return userService.getAll();
    }
}


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    void setup() {
        userRepo.deleteAll();

        User user1 = new User();
        user1.setUsername("Ana");
        user1.setEmail("ana@example.com");
        user1.setPassword("pass");
        user1.setRole("USER");
        user1.setLocation("Zagreb");

        User user2 = new User();
        user2.setUsername("Marko");
        user2.setEmail("marko@example.com");
        user2.setPassword("pass2");
        user2.setRole("ADMIN");
        user2.setLocation("Split");

        userRepo.save(user1);
        userRepo.save(user2);
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("Ana")))
                .andExpect(jsonPath("$[1].username", is("Marko")));
    }
}*/
