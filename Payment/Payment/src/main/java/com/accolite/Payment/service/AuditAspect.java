package com.accolite.Payment.service;
import com.accolite.Payment.model.AuditLog;
import com.accolite.Payment.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
        import org.aspectj.lang.annotation.AfterReturning;
        import org.aspectj.lang.annotation.Aspect;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Component;

        import java.util.Date;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "execution(com.accolite.Payment.service)", returning = "result")
    public void logServiceCall(JoinPoint joinPoint, Object result) {
        String serviceName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Date timestamp = new Date();

        AuditLog auditLog = new AuditLog();
        auditLog.setServiceName(serviceName);
        auditLog.setMethodName(methodName);
        auditLog.setTimestamp(timestamp);

        // Other fields can be set based on your requirements...

        auditLogRepository.save(auditLog);
    }
}
