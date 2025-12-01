package com.bktutor.common.entity;

import com.bktutor.common.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    private String email;

    private String phone;

    private String avatarUrl;

    private boolean isActive = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}