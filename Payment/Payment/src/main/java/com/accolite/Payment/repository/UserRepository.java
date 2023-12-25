package com.accolite.Payment.repository;

import com.accolite.Payment.model.User;
import com.accolite.Payment.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    Optional<Object> findByUserSecret(String userSecret);

    Wallet findByUser(User user);
}
