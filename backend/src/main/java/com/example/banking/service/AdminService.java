package com.example.banking.service;

import com.example.banking.dto.AuthDtos.CreateUserRequest;
import com.example.banking.entity.*;
import com.example.banking.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminService(UserRepository userRepository,
                        AccountRepository accountRepository,
                        TransactionRepository transactionRepository,
                        LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.loanRepository = loanRepository;
    }

    public User addUser(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.email()) || userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setUsername(req.username());
        user.setFullName(req.fullName());
        user.setAadhaarNumber(req.aadhaarNumber());
        user.setRole("USER");
        user = userRepository.save(user);

        BigDecimal initialBalance = req.initialBalance() != null ? req.initialBalance() : BigDecimal.ZERO;
        Account account = new Account(user, initialBalance);
        accountRepository.save(account);

        return user;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public List<Transaction> listAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> listTransactionsForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Account account = accountRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByFromAccountOrToAccount(account, account);
    }

    public List<Loan> listLoans() {
        return loanRepository.findAll();
    }

    public Loan approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus("APPROVED");
        return loanRepository.save(loan);
    }

    public Loan rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus("REJECTED");
        return loanRepository.save(loan);
    }
}
