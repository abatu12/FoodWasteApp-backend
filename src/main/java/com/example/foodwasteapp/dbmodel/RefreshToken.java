package com.example.foodwasteapp.dbmodel;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;
}
