package com.school.sms.audit.aspect;

import com.school.sms.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Audit Aspect
 * AOP aspect to automatically log certain operations
 * Can be expanded to log specific service methods
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;

    /**
     * Log all authentication operations
     */
    @AfterReturning(pointcut = "execution(* com.school.sms.auth.service.AuthService.login(..))", returning = "result")
    public void logLogin(JoinPoint joinPoint, Object result) {
        auditService.logAction("LOGIN", "User", null, "User logged in successfully");
    }

    @AfterReturning(pointcut = "execution(* com.school.sms.auth.service.AuthService.logout(..))")
    public void logLogout(JoinPoint joinPoint) {
        auditService.logAction("LOGOUT", "User", null, "User logged out");
    }

    @AfterReturning(pointcut = "execution(* com.school.sms.auth.service.AuthService.register(..))", returning = "result")
    public void logRegistration(JoinPoint joinPoint, Object result) {
        auditService.logAction("REGISTER", "User", null, "New user registered");
    }

    /**
     * Log failed authentication attempts
     */
    @AfterThrowing(pointcut = "execution(* com.school.sms.auth.service.AuthService.login(..))", throwing = "exception")
    public void logFailedLogin(JoinPoint joinPoint, Exception exception) {
        auditService.logFailedAction("LOGIN", "User", null, exception.getMessage());
    }
}
