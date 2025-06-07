package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface messageRepository extends JpaRepository<message,Long> {
}
