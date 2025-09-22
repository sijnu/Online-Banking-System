package com.example.banking.controller;

import com.example.banking.dto.AuthDtos.CreateUserRequest;
import com.example.banking.entity.Loan;
import com.example.banking.entity.Transaction;
import com.example.banking.entity.User;
import com.example.banking.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** Add new user */
    @PostMapping("/add-user")
    public ResponseEntity<User> addUser(@Valid @RequestBody CreateUserRequest req) {
        User created = adminService.addUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** List all users */
    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(adminService.listUsers());
    }

    /** List all transactions */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> allTransactions() {
        return ResponseEntity.ok(adminService.listAllTransactions());
    }

    /** List transactions for a specific user */
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> transactionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.listTransactionsForUser(userId));
    }

    /** List all loan applications */
    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> listLoans() {
        return ResponseEntity.ok(adminService.listLoans());
    }

    /** Approve a loan application */
    @PutMapping("/loans/{loanId}/approve")
    public ResponseEntity<Loan> approveLoan(@PathVariable Long loanId) {
        Loan loan = adminService.approveLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    /** Reject a loan application */
    @PutMapping("/loans/{loanId}/reject")
    public ResponseEntity<Loan> rejectLoan(@PathVariable Long loanId) {
        Loan loan = adminService.rejectLoan(loanId);
        return ResponseEntity.ok(loan);
    }
}
