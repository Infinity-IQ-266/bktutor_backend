package com.bktutor.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "student-with-department",
        attributeNodes = {
                @NamedAttributeNode("department")
        }
)
public class Student extends User {
    private String studentId;
    private String major;
    private int academicYear;
    private String className;
    private String supportNeed;
    private String learningGoals;
    private LocalDateTime enrollDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "assigned_tutor_id")
//    private Tutor assignedTutor;

    @OneToMany(mappedBy = "student")
    private List<Booking> bookings;
}
