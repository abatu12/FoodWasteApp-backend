package com.example.foodwasteapp.dbmodel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class listing {
    @Id
    private Long id;
    private String title;
    private String  description;
    private Integer quantity;
    private LocalDate  expiryDate;
    private LocalDateTime createdAt;
    private String image;
    private  Long userID;
}
