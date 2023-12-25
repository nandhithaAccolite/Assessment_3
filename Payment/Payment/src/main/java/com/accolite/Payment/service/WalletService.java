package com.accolite.Payment.service;

import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Vendor;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {


    void addFunds(User user, BigDecimal amount);
    void addOfflinePaymentAmount(User user, BigDecimal amount);
    List<String> generateOfflinePaymentCodes(User user, int numberOfCodes);
    User getUserFromToken(String token);

    BigDecimal getWalletBalance(User user);
    void addToUserWallet(User user, BigDecimal amount);
    void transferToVendorWallet(Vendor vendor, BigDecimal amount);

    // Helper method to generate unique codes (example implementation)
}
