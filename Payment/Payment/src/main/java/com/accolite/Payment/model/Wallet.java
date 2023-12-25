package com.accolite.Payment.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name="Wallet_table")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private User user;

    @OneToOne(mappedBy = "vendorWallet", cascade = CascadeType.ALL)
    private Vendor vendor;
    @OneToOne(mappedBy = "companyWallet", cascade = CascadeType.ALL)
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    private BigDecimal offlineBalance= BigDecimal.valueOf(0);

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public BigDecimal getOfflineBalance() {
        return offlineBalance;
    }

    public void setOfflineBalance(BigDecimal offlineBalance) {
        this.offlineBalance = offlineBalance;
    }

    private BigDecimal balance= BigDecimal.valueOf(0);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
