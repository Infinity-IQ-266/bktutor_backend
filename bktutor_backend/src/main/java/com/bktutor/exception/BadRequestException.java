package com.bktutor.exception;

import com.bktutor.common.enums.ErrorMessage;

public class BadRequestException extends GenericException {
    public BadRequestException(ErrorMessage error) {
        super(error.getCode(), error.getMessage(), error.getHttpStatus());
    }
    public BadRequestException(ErrorMessage error, String message) {
        super(error.getCode(), message, error.getHttpStatus());
    }

}

