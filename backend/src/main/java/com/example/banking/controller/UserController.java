package com.example.banking.controller;

import com.example.banking.entity.Account;
import com.example.banking.entity.Loan;
import com.example.banking.entity.Transaction;
import com.example.banking.service.UserService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Deposit money into account */
    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(
            Principal principal,
            @RequestParam @Positive BigDecimal amount) {
        Account updated = userService.deposit(principal, amount);
        return ResponseEntity.ok(updated);
    }

    /** Withdraw money from account */
    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(
            Principal principal,
            @RequestParam @Positive BigDecimal amount) {
        Account updated = userService.withdraw(principal, amount);
        return ResponseEntity.ok(updated);
    }

    /** Transfer money to another account */
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            Principal principal,
            @RequestParam Long toAccountId,
            @RequestParam @Positive BigDecimal amount) {
        userService.transfer(principal, toAccountId, amount);
        return ResponseEntity.ok().build();
    }

    /** Check account balance */
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(Principal principal) {
        return ResponseEntity.ok(userService.balance(principal));
    }

    /** List all transactions for the current user */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> transactions(Principal principal) {
        return ResponseEntity.ok(userService.transactions(principal));
    }

    /** Apply for a new loan */
    @PostMapping("/loan/apply")
    public ResponseEntity<Loan> applyLoan(
            Principal principal,
            @RequestParam @Positive BigDecimal amount,
            @RequestParam String reason,
            @RequestParam Integer duration) {
        Loan loan = userService.applyLoan(principal, amount, reason, duration);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    /** Check status of user's loan applications */
    @GetMapping("/loan/status")
    public ResponseEntity<List<Loan>> loanStatus(Principal principal) {
        return ResponseEntity.ok(userService.loanStatus(principal));
    }
}
