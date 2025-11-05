package com.bktutor.common.entity;

import com.bktutor.common.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type; //"IN_PERSON", "ONLINE"
    private String locationOrLink;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;
}