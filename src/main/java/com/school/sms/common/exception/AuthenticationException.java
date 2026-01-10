package com.school.sms.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for authentication failures
 */
public class AuthenticationException extends AppException {

    public AuthenticationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
