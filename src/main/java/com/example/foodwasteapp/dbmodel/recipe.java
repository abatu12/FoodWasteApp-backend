package com.example.foodwasteapp.dbmodel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
public class recipe {
    @Id
    private Long id;
    private Long userID;
    private String title;
    private String description;
    private String ingredients;
    private String steps;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String enabled;
}
