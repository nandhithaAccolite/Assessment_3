package com.accolite.Payment.service;

import com.accolite.Payment.model.User;

public interface FraudDetectionService {
    boolean isExcessiveLocationChange(String gpsLocation, User user);
    boolean hasUnusualVendorActivity(String vendorId);
}
