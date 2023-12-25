package com.accolite.Payment.service;

import com.accolite.Payment.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService{

    private static final double MAX_DISTANCE_KM = 500.0;
    private static final long MAX_TIME_DIFF_HOURS = 5;
    private static final long MAX_TRANSACTION_COUNT= 500;
    @Override
    public boolean isExcessiveLocationChange(String gpsLocation, User user) {
        String previousTimestamp=user.getGpslocation();

        // For simplicity, assume the user's last known location and timestamp
        String lastKnownLocation = "40.7128,-74.0060";  // Example: New York City
        LocalDateTime lastKnownTimestamp = LocalDateTime.now().minus(4, ChronoUnit.HOURS);
        LocalDateTime previousTimestampp = LocalDateTime.now().minus(4, ChronoUnit.HOURS);
        double distance = calculateDistance(lastKnownLocation, gpsLocation);
        long timeDiffHours;
        timeDiffHours= ChronoUnit.HOURS.between(lastKnownTimestamp, previousTimestampp);

        return distance > MAX_DISTANCE_KM && timeDiffHours < MAX_TIME_DIFF_HOURS;
    }

    @Override
        public boolean hasUnusualVendorActivity(String vendorId) {
            // Fetch vendor's transaction history from the database

            // For simplicity, assume the vendor's transaction count
            int transactionCount = 120;

            return transactionCount > MAX_TRANSACTION_COUNT;
        }

    public static double calculateDistance(String location1, String location2) {
        // Assume locations are in the format "latitude,longitude"
        String[] parts1 = location1.split(",");
        String[] parts2 = location2.split(",");

        double lat1 = Double.parseDouble(parts1[0]);
        double lon1 = Double.parseDouble(parts1[1]);
        double lat2 = Double.parseDouble(parts2[0]);
        double lon2 = Double.parseDouble(parts2[1]);

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return  c;
    }

}
