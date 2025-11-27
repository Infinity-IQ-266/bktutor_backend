package com.bktutor.common.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    USER_NOT_FOUND(404, 1001, "User not found"),
    INTERNAL_ERROR(500, 9999, "Internal server error"),
    USERNAME_ALREADY_EXISTS(400, 1003, "Username is already in use"),
    INVALID_USERNAME_PASSWORD(400, 1005, "Invalid username or password"),
    FORBIDDEN_AUTHORITY(403, 1006, "Forbidden authority"),
    DEPARTMENT_NOT_FOUND(404, 1007, "Department not found"),
    TUTOR_NOT_FOUND(404, 1008 , "Tutor not found" ),
    BAD_REQUEST(400, 1009, "Bad request"),
    AVAILABILITY_SLOT_NOT_FOUND(404, 1010, "Availability slot not found"),
    BOOKING_NOT_FOUND(404, 1011, "Booking not found"),
    MATERIAL_NOT_FOUND(404, 1012, "Material not found"),
    NOT_FOUND(404, 1013, "Not found");
    
    private final int httpStatus;
    private final int code;
    private final String message;

    ErrorMessage(int httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}

