package com.bktutor.common.dtos;

import com.bktutor.common.enums.BookingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookingDto {
    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Session type is required")
    private BookingType type; // IN_PERSON, ONLINE

    @NotBlank(message = "Location or Link is required")
    private String locationOrLink;

    private String studentNotes;
}
