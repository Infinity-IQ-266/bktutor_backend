package com.bktutor.common.dtos;


import lombok.Data;

import java.time.LocalDate;

@Data
public class FeedbackDto {
    private Long id;
    private int rating;
    private String comment;
    private LocalDate createdAt;

    private Long studentId;
    private String studentName;
    private String studentAvatar;

    private String subject;
}
