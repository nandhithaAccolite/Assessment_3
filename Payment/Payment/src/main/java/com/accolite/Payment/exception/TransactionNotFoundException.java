package com.accolite.Payment.exception;

import com.accolite.Payment.model.Transaction;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message){
        super(message);
    }

}
