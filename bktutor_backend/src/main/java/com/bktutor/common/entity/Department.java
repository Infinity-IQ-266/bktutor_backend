package com.bktutor.common.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department")
    private List<Student> students;

    @OneToMany(mappedBy = "department")
    private List<Tutor> tutors;
}
