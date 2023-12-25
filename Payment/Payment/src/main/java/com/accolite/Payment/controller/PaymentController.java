package com.accolite.Payment.controller;

import com.accolite.Payment.model.Payment;
import com.accolite.Payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/online")
    public ResponseEntity<String> processOnlinePayment(@RequestParam String userId, @RequestParam Double amount, @RequestParam String enteredOTP) {
        Payment payment = paymentService.processOnlinePayment(userId, amount, enteredOTP);
        return ResponseEntity.ok("Online payment processed successfully. Payment ID: " + payment.getId());
    }

    @PostMapping("/initiate")
    public ResponseEntity<String> processOfflinePayment(
            @RequestParam String userId,
            @RequestParam String uniqueCode,
            @RequestParam String gpsLocation,
            @RequestParam String vendorId
    ) {
        paymentService.processOfflinePayment(userId,uniqueCode, gpsLocation, vendorId);
        return ResponseEntity.ok("Offline payment processed successfully.");
    }

}

