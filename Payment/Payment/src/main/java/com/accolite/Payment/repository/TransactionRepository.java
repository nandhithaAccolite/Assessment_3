package com.accolite.Payment.repository;

import com.accolite.Payment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.accolite.Payment.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFlagged(boolean b);
}
