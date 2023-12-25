package com.accolite.Payment.service;

import com.accolite.Payment.model.Payment;
import com.accolite.Payment.model.Wallet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminServiceImpl implements AdminService{
    @Override
    public void approveOrRejectTransaction(Payment payment, BigDecimal amount, Wallet userWallet, Wallet companyWallet, Wallet vendorWallet) {
        if(payment.isPotentialFraud()){
            userWallet.setBalance(userWallet.getBalance().add(amount));
            companyWallet.setBalance(companyWallet.getBalance().subtract(amount));
            throw new RuntimeException("Not a safe transaction");
        }
        else{
            vendorWallet.setBalance(vendorWallet.getBalance().add(amount));
            companyWallet.setBalance(companyWallet.getBalance().subtract(amount));
        }
    }
}
