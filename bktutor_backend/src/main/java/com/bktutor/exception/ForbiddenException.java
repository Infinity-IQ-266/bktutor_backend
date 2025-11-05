package com.bktutor.exception;

import com.bktutor.common.enums.ErrorMessage;

public class ForbiddenException extends GenericException {
    public ForbiddenException(ErrorMessage error) {
        super(error.getCode(), error.getMessage(), error.getHttpStatus());
    }

    public ForbiddenException(ErrorMessage error, String detailMessage) {
        super(error.getCode(), detailMessage, error.getHttpStatus());
    }
}

