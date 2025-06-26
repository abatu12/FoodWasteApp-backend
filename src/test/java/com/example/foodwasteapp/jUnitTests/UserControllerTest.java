package com.example.foodwasteapp.jUnitTests;

import com.example.foodwasteapp.controller.UserController;
import com.example.foodwasteapp.dbmodel.User;
import com.example.foodwasteapp.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {


        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UserController userController;

        public UserControllerTest() {
            MockitoAnnotations.openMocks(this);
        }

    @Test
    void testGetAll_whenUsersExist_thenReturnList() {
        // Arrange
        UserRepository mockRepo = mock(UserRepository.class);
        UserController controller = new UserController(mockRepo);

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Ana");
        user1.setEmail("ana@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Marko");
        user2.setEmail("marko@example.com");

        when(mockRepo.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<User> result = controller.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).getUsername());
        assertEquals("Marko", result.get(1).getUsername());
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testGetAll_whenNoUsers_thenReturnEmptyList() {
        // Arrange
        UserRepository mockRepo = mock(UserRepository.class);
        when(mockRepo.findAll()).thenReturn(List.of());

        UserController controller = new UserController(mockRepo);

        // Act
        List<User> result = controller.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockRepo, times(1)).findAll();
    }

        @Test
        void testGetAllReturnsAllUsers() {
            // Arrange
            User u1 = new User();
            u1.setId(1L);
            u1.setUsername("john");
            u1.setEmail("john@example.com");
            u1.setRole("USER");
            u1.setLocation("Zagreb");

            User u2 = new User();
            u2.setId(2L);
            u2.setUsername("ana");
            u2.setEmail("ana@example.com");
            u2.setRole("ADMIN");
            u2.setLocation("Split");

            when(userRepository.findAll()).thenReturn(List.of(u1, u2));

            // Act
            List<User> result = userController.getAll();

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getUsername()).isEqualTo("john");
            assertThat(result.get(1).getUsername()).isEqualTo("ana");

            verify(userRepository, times(1)).findAll();
        }
    }


