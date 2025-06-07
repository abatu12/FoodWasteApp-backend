package com.example.foodwasteapp.dbmodel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
public class claim {
    @Id
    private Long id;
    private Long requesterID;
    private Long listingID;
    private String status;
    private LocalDateTime requestedAt;

}

