package com.example.banking.repository;

import com.example.banking.entity.Account;
import com.example.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccount(Account account);
    List<Transaction> findByFromAccountOrToAccount(Account from, Account to);
}
