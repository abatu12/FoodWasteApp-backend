package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
