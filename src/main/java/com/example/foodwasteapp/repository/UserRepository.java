package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
