package com.accolite.Payment.service;

import com.accolite.Payment.model.Payment;
import com.accolite.Payment.model.User;

import java.math.BigDecimal;

public interface PaymentService {
    Payment processOnlinePayment(String userId, Double amount, String enteredOTP);

    void processOfflinePayment(String userId, BigDecimal amount, String uniqueCode, String gpsLocation, String vendorId);
}
