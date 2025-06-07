package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<user, Long> {
}
