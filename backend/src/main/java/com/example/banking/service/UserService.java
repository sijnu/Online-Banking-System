package com.example.banking.service;

import com.example.banking.entity.*;
import com.example.banking.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;

    public UserService(UserRepository userRepository,
                       AccountRepository accountRepository,
                       TransactionRepository transactionRepository,
                       LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.loanRepository = loanRepository;
    }

    private Account getAccountForPrincipal(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account deposit(Principal principal, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        Account account = getAccountForPrincipal(principal);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setFromAccount(account);
        txn.setAmount(amount);
        txn.setType("DEPOSIT");
        transactionRepository.save(txn);

        return account;
    }

    public Account withdraw(Principal principal, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        Account account = getAccountForPrincipal(principal);

        if (account.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Insufficient balance");

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setFromAccount(account);
        txn.setAmount(amount);
        txn.setType("WITHDRAW");
        transactionRepository.save(txn);

        return account;
    }

    public void transfer(Principal principal, Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        Account from = getAccountForPrincipal(principal);
        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        if (from.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Insufficient balance");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accountRepository.save(from);
        accountRepository.save(to);

        Transaction txn = new Transaction();
        txn.setFromAccount(from);
        txn.setToAccount(to);
        txn.setAmount(amount);
        txn.setType("TRANSFER");
        transactionRepository.save(txn);
    }

    public BigDecimal balance(Principal principal) {
        return getAccountForPrincipal(principal).getBalance();
    }

    public List<Transaction> transactions(Principal principal) {
        Account account = getAccountForPrincipal(principal);
        return transactionRepository.findByFromAccountOrToAccount(account, account);
    }

    public Loan applyLoan(Principal principal, BigDecimal amount, String reason, Integer duration) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAmount(amount);
        loan.setReason(reason);
        loan.setDuration(duration);
        loan.setStatus("PENDING");
        return loanRepository.save(loan);
    }

    public List<Loan> loanStatus(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return loanRepository.findByUser(user);
    }
}
