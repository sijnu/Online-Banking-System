package com.example.banking.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AuthDtos {
    public record LoginRequest(
            @NotBlank String email,
            @NotBlank String password) {
    }

    public record JwtResponse(
            String token,
            String role,
            Long userId) {
    }

    public record CreateUserRequest(
            @NotBlank String email,
            @NotBlank String password,
            @NotBlank String username,
            @NotBlank String fullName,
            @NotBlank String aadhaarNumber,
            @PositiveOrZero BigDecimal initialBalance) {
    }
}
