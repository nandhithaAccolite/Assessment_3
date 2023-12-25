package com.accolite.Payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.accolite.Payment.model.AuditLog;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
