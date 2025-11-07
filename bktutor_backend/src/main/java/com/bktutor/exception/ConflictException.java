package com.bktutor.exception;

import com.bktutor.common.enums.ErrorMessage;

public class ConflictException extends GenericException {
    public ConflictException(ErrorMessage error) {
        super(error.getCode(), error.getMessage(), error.getHttpStatus());
    }
    public ConflictException(ErrorMessage error, String message) {
        super(error.getCode(), message, error.getHttpStatus());
    }
}
