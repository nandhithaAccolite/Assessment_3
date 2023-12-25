package com.accolite.Payment.service;

import com.accolite.Payment.exception.OfflinePaymentLimitExceeded;
import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Vendor;
import com.accolite.Payment.model.Wallet;
import com.accolite.Payment.repository.UserRepository;
import com.accolite.Payment.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private WalletRepository walletRepository;

    public void addFunds(User user, BigDecimal amount) {
        Wallet wallet = user.getWallet();
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
    }
    @Override
    public void addOfflinePaymentAmount(User user, BigDecimal amount) {
        Wallet wallet = user.getWallet();
        BigDecimal newBalance = wallet.getOfflineBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.valueOf(5000)) <= 0) {
            wallet.setOfflineBalance(newBalance);
/*            wallet.setBalance(wallet.getBalance().subtract(newBalance));*/
        } else {
            throw new OfflinePaymentLimitExceeded("Offline limit only 5000");
        }
    }

    public List<String> generateOfflinePaymentCodes(User user, int numberOfCodes) {
        Wallet wallet = user.getWallet();
        BigDecimal balance = wallet.getBalance();
        List<String> generatedCodes = new ArrayList<>();
        if (user.isApproved() && balance.compareTo(BigDecimal.ZERO) > 0)
        {

            for (int i = 0; i < numberOfCodes; i++)
            {
                String code = generateUniqueCode();
                generatedCodes.add(code);

            }
        }
        else
        {
            throw new RuntimeException("Insufficient funds for generating offline payment codes");
        }
        for (String code : generatedCodes) {
            user.addOfflinePaymentCode(code);
        }

        // Save the user entity to persist the codes
        userRepository.save(user);
        return generatedCodes;
    }

    @Override
    public User getUserFromToken(String token) {
        String userId = userService.getUserIdFromToken(token);

        // Assuming you have a method to fetch user by userId from a user repository
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user;
    }

    @Override
    public BigDecimal getWalletBalance(User user) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found for the user"));

        return wallet.getBalance();
    }

    private static String generateUniqueCode() {
        // Generate a UUID and remove hyphens to create a unique code
        String code = UUID.randomUUID().toString().replace("-", "");
        return code;
    }
    @Override
    public void addToUserWallet(User user, BigDecimal amount) {

        Wallet userWallet = userRepository.findByUser(user);
        if (userWallet != null) {
            BigDecimal newBalance = userWallet.getBalance().add(amount);
            userWallet.setBalance(newBalance);

            // Save the updated user wallet
            walletRepository.save(userWallet);
        } else {
            createWalletForUser(user, amount);
        }
    }
    private void createWalletForUser(User user, BigDecimal initialBalance) {
        Wallet newWallet = new Wallet();
        newWallet.setUser(user);
        newWallet.setBalance(initialBalance);
        walletRepository.save(newWallet);
    }
    @Override

    public void transferToVendorWallet(Vendor vendor, BigDecimal amount) {
        // Assuming there's a single wallet for the company, retrieve it
        Wallet companyWallet = walletRepository.findCompanyWallet();

        if (companyWallet != null && companyWallet.getBalance().compareTo(amount) >= 0) {
            // Subtract the amount from the company's wallet balance
            BigDecimal newCompanyBalance = companyWallet.getBalance().subtract(amount);
            companyWallet.setBalance(newCompanyBalance);

            // Save the updated company wallet
            walletRepository.save(companyWallet);

            // Now, transfer the funds to the vendor's wallet
            Wallet vendorWallet = walletRepository.findByVendor(vendor);
            if (vendorWallet != null) {
                BigDecimal newVendorBalance = vendorWallet.getBalance().add(amount);
                vendorWallet.setBalance(newVendorBalance);
                // Save the updated vendor wallet
                walletRepository.save(vendorWallet);
            } else {
                createWalletForVendor(vendor, amount);
            }
        } else {
            // Handle insufficient funds in the company's wallet
            // You might want to throw an exception or log a warning
        }
    }

    // Additional methods...

    private void createWalletForVendor(Vendor vendor, BigDecimal initialBalance) {
        Wallet newWallet = new Wallet();
        newWallet.setVendor(vendor);
        newWallet.setBalance(initialBalance);

        // Save the new vendor wallet
        walletRepository.save(newWallet);
    }
}

