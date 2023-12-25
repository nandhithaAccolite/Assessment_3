package com.accolite.Payment.exception;

public class OfflinePaymentLimitExceeded extends RuntimeException {
    public OfflinePaymentLimitExceeded(String message){
        super(message);
    }
}