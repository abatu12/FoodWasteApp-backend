package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.RefreshToken;
import com.example.foodwasteapp.dbmodel.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    @Transactional
    @Query("delete from RefreshToken rt where rt.user = :user")
    void deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("delete from RefreshToken rt where rt.user.id = :userId")
    void deleteByUserId(Long userId);
}

