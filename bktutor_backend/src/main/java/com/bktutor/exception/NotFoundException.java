package com.bktutor.exception;

import com.bktutor.common.enums.ErrorMessage;

public class NotFoundException extends GenericException {
    public NotFoundException(ErrorMessage error) {
        super(error.getCode(), error.getMessage(), error.getHttpStatus());
    }
    public NotFoundException(ErrorMessage error, String message) {
        super(error.getCode(), message, error.getHttpStatus());
    }

}

