package com.example.banking.controller;

import com.example.banking.dto.AuthDtos.LoginRequest;
import com.example.banking.dto.AuthDtos.JwtResponse;
import com.example.banking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login endpoint
     * Input: email & password
     * Output: JWT token + role + userId
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest req) {
        JwtResponse response = authService.login(req);
        return ResponseEntity.ok(response);
    }
}
