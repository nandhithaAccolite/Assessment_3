package com.accolite.Payment.service;

import com.accolite.Payment.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.accolite.Payment.model.Transaction;
import java.util.List;

public class AdminOversightServiceImpl implements AdminOversightService{
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private WalletService walletService;
    @Override
    public void reviewAndProcessTransactions() {
        List<Transaction> flaggedTransactions = transactionService.getFlaggedTransactions();

        for (Transaction transaction : flaggedTransactions) {
            if (transaction.isFlagged()) {
                // Admin rejects the transaction
                rejectTransaction(transaction);
            } else {
                // Admin approves the transaction
                approveTransaction(transaction);
            }
        }

    }
    private void rejectTransaction(Transaction transaction) {
        // Return the amount to the user's wallet
        walletService.addToUserWallet(transaction.getUser(), transaction.getAmount());
        // Update transaction status
        transactionService.updateTransactionStatus(transaction.getId(), TransactionStatus.REJECTED);
    }

    private void approveTransaction(Transaction transaction) {
        // Transfer the amount to the vendor's wallet
        walletService.transferToVendorWallet(transaction.getVendor(), transaction.getAmount());
        // Update transaction status
        transactionService.updateTransactionStatus(transaction.getId(), TransactionStatus.APPROVED);
    }
}
