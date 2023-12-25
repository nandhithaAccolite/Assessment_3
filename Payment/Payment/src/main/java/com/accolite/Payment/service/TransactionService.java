package com.accolite.Payment.service;

import com.accolite.Payment.model.Transaction;
import com.accolite.Payment.model.TransactionStatus;
import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Vendor;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();
    List<Transaction> getFlaggedTransactions();
    Transaction createTransaction(User user, Vendor vendor, BigDecimal amount);
    void updateTransactionStatus(Long transactionId, TransactionStatus status);


    void flagTransaction(Long transactionId);
}
