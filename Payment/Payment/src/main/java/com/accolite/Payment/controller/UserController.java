package com.accolite.Payment.controller;

import com.accolite.Payment.dto.ExchangeTokenDTO;
import com.accolite.Payment.dto.RegistrationDTO;
import com.accolite.Payment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register/{userId}")
    public ResponseEntity<String> registerUser(@PathVariable String userId) {
        String userSecret = userService.registerUser(userId);
        return ResponseEntity.ok("User registered successfully. User Secret: " + userSecret);
    }

    @PostMapping("/getSecret/{userId}")
    public ResponseEntity<String> getUserSecret(@PathVariable String userId) {
        String userSecret = userService.getUserSecretByUserId(userId);
        return ResponseEntity.ok("User Secret: " + userSecret);
    }

    @PostMapping("/exchange-token")
    public ResponseEntity<String> exchangeUserSecretForToken(@RequestBody ExchangeTokenDTO exchangeTokenDTO) {
        String token = userService.exchangeUserSecretForToken(exchangeTokenDTO.getUserSecret());
        return ResponseEntity.ok("Token: " + token);
    }

    @PostMapping("/enroll-offline-payment")
    public ResponseEntity<String> enrollForOfflinePayment(@RequestHeader("Authorization") String token) {
        String userId = userService.getUserIdFromToken(token);
        userService.enrollForOfflinePayment(userId);
        return ResponseEntity.ok("Enrolled for offline payment successfully.");
    }
    @GetMapping("/check-functionality-eligibility")
    public ResponseEntity<String> checkFunctionalityEligibility(@RequestHeader("Authorization") String token) {
        String userId = userService.getUserIdFromToken(token);
        if (userService.isUserAllowedToUseFunctionality(userId)) {
            return ResponseEntity.ok("User is eligible to use the functionality.");
        } else {
            return ResponseEntity.ok("User is not yet eligible to use the functionality.");
        }
    }
    //POST /users/switch-offline-payments?enable=true&latitude=12.971598&longitude=77.594562

    @PostMapping("/switch-offline-payments")
    public ResponseEntity<String> switchOfflinePayments(
            @RequestHeader("Authorization") String token,
            @RequestParam boolean enable,
            @RequestParam(required = false) String gpsLocation ){
        String userId = userService.getUserIdFromToken(token);

        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Call the service method to switch offline payments and provide GPS location only when enabling
        userService.switchOfflinePayments(userId,enable,gpsLocation);

        return ResponseEntity.ok("Offline payments switched " + (enable ? "on" : "off") + " successfully.");
    }

}
