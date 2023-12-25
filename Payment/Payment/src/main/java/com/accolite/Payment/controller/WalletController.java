package com.accolite.Payment.controller;

import com.accolite.Payment.dto.OfflinePaymentCodeResponseDTO;
import com.accolite.Payment.model.User;
import com.accolite.Payment.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/add-funds/{amount}")
    public ResponseEntity<String> addFunds(@PathVariable BigDecimal amount,
                                           @RequestHeader("Authorization") String token) {
        User user = walletService.getUserFromToken(token);
        walletService.addFunds(user, amount);
        return ResponseEntity.ok("Funds added successfully.");
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getWalletBalance(@RequestHeader("Authorization") String token) {
        User user = walletService.getUserFromToken(token);
        BigDecimal balance = walletService.getWalletBalance(user);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/generate-offline-payment-codes/{numberOfCodes}")
    public ResponseEntity<List<OfflinePaymentCodeResponseDTO>> generateOfflinePaymentCodes(
            @PathVariable int numberOfCodes, @RequestHeader("Authorization") String token) {
        User user = walletService.getUserFromToken(token);
        List<String> codes = walletService.generateOfflinePaymentCodes(user, numberOfCodes);

        // Assuming you have a DTO for response
        List<OfflinePaymentCodeResponseDTO> responseDTOs = mapToResponseDTOs(codes);

        return ResponseEntity.ok(responseDTOs);
    }

    // Helper method to map codes to response DTOs
    private List<OfflinePaymentCodeResponseDTO> mapToResponseDTOs(List<String> codes) {
        return codes.stream()
                .map(OfflinePaymentCodeResponseDTO::new)
                .collect(Collectors.toList());
    }

}

