package com.accolite.Payment.repository;
import com.accolite.Payment.model.Payment;
import com.accolite.Payment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findLatestOnlinePaymentByUserId(Long id);

    Payment findByUserAndPaymentCode(User user, String uniqueCode);
}