package com.bktutor.common.dtos;

import com.bktutor.common.enums.BookingStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private String subject;
    private String type;
    private String locationOrLink;
    private String studentNotes;
    private BookingStatus status;

    // Thông tin về Slot
    private Long slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Thông tin về Student
    private Long studentId;
    private String studentName;

    // Thông tin về Tutor
    private Long tutorId;
    private String tutorName;
}