package com.accolite.Payment.service;

import com.accolite.Payment.exception.PaymentNotFoundException;
import com.accolite.Payment.exception.UserNotFoundException;
import com.accolite.Payment.model.Payment;
import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Wallet;
import com.accolite.Payment.repository.PaymentRepository;
import com.accolite.Payment.repository.UserRepository;
import com.accolite.Payment.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Transactional
    public Payment processOnlinePayment(String userId, Double amount, String enteredOTP) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payment onlinePayment = createOnlinePayment(user, amount);
        return paymentRepository.save(onlinePayment);

    }

    @Override
    public void processOfflinePayment(String userId, BigDecimal amount,String uniqueCode, String gpsLocation, String vendorId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Optional<Wallet> wallet = walletRepository.findByUser(user);
        Wallet vendorWallet = walletRepository.findByVendorId(vendorId);
        Wallet companyWallet = walletRepository.findCompanyWallet();

        if(user.hasOfflinePaymentCode(uniqueCode)){
            // Retrieve the payment by paymentCode and user
            Payment payment = new Payment();

            // Update GPS location and vendor ID
            payment.setGpsLocation(gpsLocation);
            payment.setVendorId(vendorId);

            Wallet userWallet = wallet.get();
            userWallet.setBalance(userWallet.getBalance().subtract(amount));
            companyWallet.setBalance(companyWallet.getBalance().add(amount));
            // Check for potential fraud
            detectFraud(payment);
            adminService.approveOrRejectTransaction(payment,amount,userWallet,companyWallet,vendorWallet);
            // Save the Payment entity
            paymentRepository.save(payment);

        }
        else{
            throw new RuntimeException("Specified code is not present ");
        }


    }
    private void detectFraud(Payment payment) {
        // For simplicity, let's assume potential fraud if any condition is met
        if (fraudDetectionService.isExcessiveLocationChange(payment.getGpsLocation(), payment.getUser())
                || fraudDetectionService.hasUnusualVendorActivity(payment.getVendorId())) {
            payment.setPotentialFraud(true);
        }
    }

    private Payment createOnlinePayment(User user, Double amount) {
        // Generate a new OTP for the online payment
        String newOTP = generateRandomOTP();

        // Create a new online payment
        Payment onlinePayment = new Payment();
        onlinePayment.setPaymentType("online");
        onlinePayment.setAmount(amount);
        onlinePayment.setStatus("pending");
        onlinePayment.setOtp(newOTP);
        onlinePayment.setUser(user);

        return onlinePayment;
    }

    private String generateRandomOTP() {
        // For simplicity, a random 6-digit numeric OTP is generated here.
        return String.format("%06d", new Random().nextInt(1000000));
    }




}
