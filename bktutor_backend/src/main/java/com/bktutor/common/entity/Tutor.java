package com.bktutor.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tutors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tutor extends User {
    private String staffId;
    private String position;
    private String degree;
    @Column(columnDefinition = "TEXT")
    private String bio;
    @Column(columnDefinition = "TEXT")
    private String expertise;
    private float averageRating;
    private String officeLocation;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "assignedTutor")
    private List<Student> assignedStudents;

    @OneToMany(mappedBy = "tutor")
    private List<AvailabilitySlot> availabilitySlots;

    @OneToMany(mappedBy = "tutor")
    private List<Booking> bookings;
}