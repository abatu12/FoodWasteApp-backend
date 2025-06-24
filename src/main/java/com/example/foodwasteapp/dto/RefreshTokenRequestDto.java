package com.example.foodwasteapp.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
@Data
public class RefreshTokenRequestDto {
    @NotBlank private String refreshToken;
}
