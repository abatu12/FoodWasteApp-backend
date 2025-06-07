package com.example.foodwasteapp.dbmodel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
public class message {
    @Id
    private Long id;
    private Long senderID;
    private Long receiverID;
    private Long listingID;
    private String text;
    private LocalDateTime timestamp;
}
