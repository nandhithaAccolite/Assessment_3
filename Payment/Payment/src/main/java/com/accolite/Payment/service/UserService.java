package com.accolite.Payment.service;

import java.time.LocalDateTime;

public interface UserService {
    String registerUser(String registrationDTO);

    void enrollForOfflinePayment(String userId);

    boolean isUserAllowedToUseFunctionality(String userId);

    String getUserSecretByUserId(String userId);

    String getUserIdFromToken(String token);

    String exchangeUserSecretForToken(String userSecret);

    void approveRegistration(String userId);

    void switchOfflinePayments(String userId, boolean enableOfflinePayments, String gpsLocation);
    public boolean isCooldownPeriodElapsed(String userId, LocalDateTime currentDateTime);

}
