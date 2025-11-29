package com.bktutor.common.entity;

import com.bktutor.common.enums.MaterialSource;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "materials")
@Data
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String fileType;
    private Long fileSize;
    private String s3Key;
    private String externalUrl;

    private String originalFilename;

    @Enumerated(EnumType.STRING)
    private MaterialSource source;

    @CreationTimestamp
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    private String subjectName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "material_recipients",
            joinColumns = @JoinColumn(name = "material_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> recipients = new HashSet<>();
}