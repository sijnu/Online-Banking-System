package com.example.banking.service;

import com.example.banking.dto.AuthDtos.JwtResponse;
import com.example.banking.dto.AuthDtos.LoginRequest;
import com.example.banking.entity.Account;
import com.example.banking.entity.User;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.UserRepository;
import com.example.banking.config.JwtService;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, AccountRepository accountRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
    }

    // Seed default admin if not exists
    @PostConstruct
    public void seedAdmin() {
        userRepository.findByEmail("admin@bank.com").orElseGet(() -> {
            User admin = new User();
            admin.setEmail("admin@bank.com");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
            admin.setUsername("admin");
            admin.setFullName("System Admin");
            admin.setAadhaarNumber("0000-0000-0000");
            admin.setRole("ADMIN");
            User savedAdmin = userRepository.save(admin);

            Account account = new Account(savedAdmin, BigDecimal.ZERO);
            accountRepository.save(account);
            return savedAdmin;
        });
    }

    public JwtResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail(), Map.of(
                "role", user.getRole(),
                "userId", user.getUserId()
        ));

        return new JwtResponse(token, user.getRole(), user.getUserId());
    }
}
