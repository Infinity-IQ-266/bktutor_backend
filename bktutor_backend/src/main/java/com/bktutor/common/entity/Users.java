package com.bktutor.common.entity;

import com.bktutor.common.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String fullName;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;  

    @CreationTimestamp
    private LocalDateTime createdDate;

    @CreationTimestamp
    private LocalDateTime updatedDate;

    private String faculty;

}
