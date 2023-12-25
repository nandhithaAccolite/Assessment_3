package com.accolite.Payment.service;

import com.accolite.Payment.model.Payment;
import com.accolite.Payment.model.Wallet;

import java.math.BigDecimal;

public interface AdminService {
    void approveOrRejectTransaction(Payment payment, BigDecimal amount, Wallet userWallet, Wallet companyWallet, Wallet vendorWallet);
}
