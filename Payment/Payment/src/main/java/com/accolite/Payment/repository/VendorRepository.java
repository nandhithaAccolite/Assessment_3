package com.accolite.Payment.repository;

import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

}