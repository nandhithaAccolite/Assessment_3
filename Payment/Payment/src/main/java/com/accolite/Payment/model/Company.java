package com.accolite.Payment.model;

import javax.persistence.*;

@Entity
@Table(name="company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "companyWallet_id", referencedColumnName = "id")
    private Wallet companyWallet;
}
