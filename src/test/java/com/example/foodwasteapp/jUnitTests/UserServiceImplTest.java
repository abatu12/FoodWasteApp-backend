package com.example.foodwasteapp.jUnitTests;

import com.example.foodwasteapp.dbmodel.User;
import com.example.foodwasteapp.dto.UserDto;
import com.example.foodwasteapp.repository.UserRepository;
import com.example.foodwasteapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testGetAllUsers() {
        User u = new User();
        u.setId(1L);
        u.setUsername("testuser");
        u.setEmail("test@example.com");
        u.setRole("USER");
        u.setLocation("Zagreb");

        when(userRepository.findAll()).thenReturn(List.of(u));

        List<UserDto> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void testGetById_success() {
        User u = new User();
        u.setId(1L);
        u.setUsername("user1");
        u.setEmail("user1@example.com");
        u.setRole("ADMIN");
        u.setLocation("Split");

        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        UserDto dto = userService.getById(1L);

        assertEquals("user1", dto.getUsername());
    }

    @Test
    void testGetById_notFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getById(2L));
    }

    @Test
    void testCreateUser() {
        UserDto dto = new UserDto();
        dto.setUsername("created");
        dto.setEmail("c@example.com");
        dto.setRole("USER");
        dto.setLocation("Osijek");

        User savedUser = new User();
        savedUser.setId(10L);
        savedUser.setUsername("created");
        savedUser.setEmail("c@example.com");
        savedUser.setRole("USER");
        savedUser.setLocation("Osijek");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.create(dto);

        assertEquals("created", result.getUsername());
        assertEquals("c@example.com", result.getEmail());
    }

    @Test
    void testUpdateUser_success() {
        Long id = 1L;
        User existing = new User();
        existing.setId(id);
        existing.setUsername("old");
        existing.setEmail("old@example.com");
        existing.setRole("USER");
        existing.setLocation("Zadar");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));

        User updated = new User();
        updated.setId(id);
        updated.setUsername("new");
        updated.setEmail("new@example.com");
        updated.setRole("ADMIN");
        updated.setLocation("Rijeka");

        when(userRepository.save(any(User.class))).thenReturn(updated);

        UserDto dto = new UserDto();
        dto.setUsername("new");
        dto.setEmail("new@example.com");
        dto.setRole("ADMIN");
        dto.setLocation("Rijeka");

        UserDto result = userService.update(id, dto);

        assertEquals("new", result.getUsername());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void testUpdateUser_notFound() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        UserDto dto = new UserDto();
        dto.setUsername("x");

        assertThrows(NoSuchElementException.class, () -> userService.update(5L, dto));
    }

    @Test
    void testDeleteUser() {
        userService.delete(3L);
        verify(userRepository, times(1)).deleteById(3L);
    }
}
