package com.bktutor.common.dtos;

import lombok.Data;

@Data
public class TutorDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private String staffId;
    private String position;
    private String degree;
    private String bio;
    private String expertise;
    private float averageRating;
    private String officeLocation;
    private String departmentName;
}
