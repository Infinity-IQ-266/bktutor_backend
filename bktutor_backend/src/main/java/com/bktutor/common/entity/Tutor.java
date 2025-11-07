package com.bktutor.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tutor_subjects",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Subject> subjects;

    @OneToMany(mappedBy = "tutor")
    private List<AvailabilitySlot> availabilitySlots;
}