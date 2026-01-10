package com.school.sms.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for bad request scenarios
 */
public class BadRequestException extends AppException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }
}
