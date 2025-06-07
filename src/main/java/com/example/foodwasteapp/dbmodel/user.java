package com.example.foodwasteapp.dbmodel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class user {
    @Id
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String  location;
}
