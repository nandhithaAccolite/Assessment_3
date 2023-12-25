package com.accolite.Payment.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="User_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(unique = true)
    private String userId;
    private String userSecret;
    private boolean userEnrolled;
    private boolean isApproved;
    private boolean offlinePaymentsEnabled;
    private LocalDateTime offlinePaymentsDisabledTime;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    @Column(name = "enrollment_timestamp")
    private LocalDateTime enrollmentTimestamp;

    private String gpslocation;

    @ElementCollection
    @CollectionTable(name = "offline_payment_codes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "code")
    private List<String> offlinePaymentCodes;
    public List<Payment> getPayments() {
        return payments;
    }

    // Constructors, getters, setters, etc.

    // Add a method to add codes to the user
    public void addOfflinePaymentCode(String code) {
        if (offlinePaymentCodes == null) {
            offlinePaymentCodes = new ArrayList<>();
        }
        offlinePaymentCodes.add(code);
    }

    public List<String> getOfflinePaymentCodes() {
        return offlinePaymentCodes;
    }
    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String getGpslocation() {
        return gpslocation;
    }

    public void setGpslocation(String gpslocation) {
        this.gpslocation = gpslocation;
    }


    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    public boolean isUserEnrolled() {
        return userEnrolled;
    }

    public void setUserEnrolled(boolean userEnrolled) {
        this.userEnrolled = userEnrolled;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public LocalDateTime getEnrollmentTimestamp() {
        return enrollmentTimestamp;
    }

    public void setEnrollmentTimestamp(LocalDateTime enrollmentTimestamp) {
        this.enrollmentTimestamp = enrollmentTimestamp;
    }

    public boolean isOfflinePaymentsEnabled() {
        return offlinePaymentsEnabled;
    }

    public void setOfflinePaymentsEnabled(boolean offlinePaymentsEnabled) {
        this.offlinePaymentsEnabled = offlinePaymentsEnabled;
    }

    public LocalDateTime getOfflinePaymentsDisabledTime() {
        return offlinePaymentsDisabledTime;
    }

    public void setOfflinePaymentsDisabledTime(LocalDateTime offlinePaymentsDisabledTime) {
        this.offlinePaymentsDisabledTime = offlinePaymentsDisabledTime;
    }
    public boolean hasOfflinePaymentCode(String code) {
        return offlinePaymentCodes.stream()
                .anyMatch(paymentCode -> paymentCode.equals(code));
    }


}
