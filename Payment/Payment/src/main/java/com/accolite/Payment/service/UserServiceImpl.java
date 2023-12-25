package com.accolite.Payment.service;

import com.accolite.Payment.exception.UserAlreadyEnrolledException;
import com.accolite.Payment.exception.UserNotFoundException;
import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Wallet;
import com.accolite.Payment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import com.accolite.Payment.exception.UserAlreadyExistException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public String registerUser(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException("User already exist");
        }
        else{
            User user = new User();
            user.setUserId(userId);
            user.setUserSecret(generateUserSecret(userId));
            user.setUserEnrolled(false);
            user.setApproved(false);
            Wallet wallet = new Wallet();
            user.setWallet(wallet);
            userRepository.save(user);
            return user.getUserSecret();
        }
    }

    public String generateUserSecret(String userId) {
        try {
            // Generate a random string
            String randomString = generateRandomString();

            // Concatenate userId and random string
            String concatenatedString = userId + randomString;

            // Hash the concatenated string using SHA-256
            String hashedString = hashString(concatenatedString);

            return hashedString;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating user secret", e);
        }
    }
    private static String generateRandomString() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        // Convert bytes to hexadecimal representation
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : randomBytes) {
            stringBuilder.append(String.format("%02x", b));
        }

        return stringBuilder.toString();
    }

    private static String hashString(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = messageDigest.digest(input.getBytes());

        // Convert bytes to hexadecimal representation
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashBytes) {
            stringBuilder.append(String.format("%02x", b));
        }

        return stringBuilder.toString();
    }

    @Override
    public void enrollForOfflinePayment(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.isUserEnrolled()) {
                user.setUserEnrolled(true);
                user.setEnrollmentTimestamp(LocalDateTime.now());
                approveRegistration(user.getUserId());
                userRepository.save(user);
            } else {
                // Handle user not approved or already enrolled
                 throw new UserAlreadyEnrolledException("User Already Enrolled");
            }
        } else {
            throw  new UserNotFoundException("Specified user not present");
        }
    }

    @Override
    public boolean isUserAllowedToUseFunctionality(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isApproved() && user.isUserEnrolled() && isWaitingPeriodOver(user)) {
                return true;
            }
        }
        return false;
    }
    private boolean isWaitingPeriodOver(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime enrollmentTimestamp = user.getEnrollmentTimestamp();
        return enrollmentTimestamp.plusMinutes(15).isBefore(now);
    }
    @Override
    public String getUserSecretByUserId(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String userSecret = user.getUserSecret();
            return userSecret;
        } else {
            // Handle the case where the user with the given userId is not found
            return null; // Or throw an exception, return a default value, etc.
        }
    }

    @Override

    public String getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }
    @Override
    public String exchangeUserSecretForToken(String userSecret) {
        // Validate userSecret and retrieve the corresponding user
        User user = (User) userRepository.findByUserSecret(userSecret).orElseThrow(() -> new RuntimeException("Invalid user secret"));

        // Check additional conditions if needed (e.g., user approval, waiting period)

        // Generate a JWT token for the user
        String token = jwtTokenProvider.generateToken(user.getUserId());

        return token;
    }

    @Override
    public void approveRegistration(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the user is not already approved
            if (!user.isApproved()) {
                user.setApproved(true);
                userRepository.save(user);
            } else {
                // Handle the case where the user is already approved
                throw new RuntimeException("User is already approved.");
            }
        } else {
            throw new UserNotFoundException("Specified user not present");
        }
    }

    @Override
    public void switchOfflinePayments(String userId, boolean enableOfflinePayments, String gpsLocation) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Validate user approval and last toggle time
        if (user.isApproved() && isCooldownPeriodElapsed(user.getUserId(),user.getOfflinePaymentsDisabledTime()) )
        {
            user.setOfflinePaymentsEnabled(enableOfflinePayments);
            if (enableOfflinePayments)
            {
                user.setGpslocation(gpsLocation);
            }
            else
            {
                // Clear GPS location when disabling offline payments
                user.setGpslocation(null);

            }
            user.setOfflinePaymentsDisabledTime(LocalDateTime.now());
            userRepository.save(user);
        }
        else {
            throw new RuntimeException("User is not authorized to toggle offline payments at this time.");
        }
    }
    @Override
    public boolean isCooldownPeriodElapsed(String userId, LocalDateTime currentDateTime) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isOfflinePaymentsEnabled()) {
            LocalDateTime disabledTime = user.getOfflinePaymentsDisabledTime();
            if (disabledTime != null) {
                LocalDateTime cooldownEndTime = disabledTime.plusMinutes(15);
                return currentDateTime.isAfter(cooldownEndTime);
            }
        }
        return true;
    }

}
