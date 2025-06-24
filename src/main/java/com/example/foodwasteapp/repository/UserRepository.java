package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(@NotBlank String username);

    boolean existsByEmail(@NotBlank @Email String email);
}
