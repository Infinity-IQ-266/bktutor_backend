package com.bktutor.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "subjects")
@Data
public class Subject extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String code; // Ví dụ: CO3001

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "subjects")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Tutor> tutors;
}