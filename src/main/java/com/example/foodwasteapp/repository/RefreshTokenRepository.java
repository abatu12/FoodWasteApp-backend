package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.RefreshToken;
import com.example.foodwasteapp.dbmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
