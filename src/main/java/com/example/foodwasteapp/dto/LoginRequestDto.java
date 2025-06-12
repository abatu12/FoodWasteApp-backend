package com.example.foodwasteapp.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequestDto {
    @NotBlank private String username;
    @NotBlank private String password;
}
