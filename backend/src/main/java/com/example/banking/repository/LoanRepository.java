package com.example.banking.repository;

import com.example.banking.entity.Loan;
import com.example.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
}
