package com.accolite.Payment.repository;


import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Vendor;
import com.accolite.Payment.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);

    Wallet findCompanyWallet();

    Wallet findByVendor(Vendor vendor);

    Wallet findByVendorId(String vendorId);
}