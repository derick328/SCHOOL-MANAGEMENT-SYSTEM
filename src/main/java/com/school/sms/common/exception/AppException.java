package com.school.sms.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception for application-specific exceptions
 */
@Getter
public class AppException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public AppException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
    }
}
