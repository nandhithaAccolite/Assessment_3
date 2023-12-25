package com.accolite.Payment.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Vendor_table")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(unique = true)
    private String vendorId;
    private String vendorSecret;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Payment> payments;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vendorWallet_id", referencedColumnName = "id")
    private Wallet vendorWallet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorSecret() {
        return vendorSecret;
    }

    public void setVendorSecret(String vendorSecret) {
        this.vendorSecret = vendorSecret;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Wallet getVendorWallet() {
        return vendorWallet;
    }

    public void setVendorWallet(Wallet vendorWallet) {
        this.vendorWallet = vendorWallet;
    }
}
