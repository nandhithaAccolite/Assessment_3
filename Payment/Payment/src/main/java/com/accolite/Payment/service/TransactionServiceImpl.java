package com.accolite.Payment.service;

import com.accolite.Payment.exception.TransactionNotFoundException;
import com.accolite.Payment.model.Transaction;
import com.accolite.Payment.model.TransactionStatus;
import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Vendor;
import com.accolite.Payment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getFlaggedTransactions() {
        return transactionRepository.findByFlagged(true);
    }

    @Override
    public Transaction createTransaction(User user, Vendor vendor, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setVendor(vendor);
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setFlagged(false);

        // Save the transaction to the database
        return transactionRepository.save(transaction);
    }

    @Override
    public void updateTransactionStatus(Long transactionId, TransactionStatus status) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setStatus(status);
            transactionRepository.save(transaction);
        } else {
            throw new TransactionNotFoundException("Transaction not found with ID: " + transactionId);
        }
    }

    @Override
    public void flagTransaction(Long transactionId) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setFlagged(true);
            transactionRepository.save(transaction);
        } else {
            throw new TransactionNotFoundException("Transaction not found with ID: " + transactionId);
        }
    }
    }

