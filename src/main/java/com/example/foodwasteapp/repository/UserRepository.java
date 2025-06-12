package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
