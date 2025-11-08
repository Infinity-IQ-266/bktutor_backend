package com.bktutor.common.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class TutorDetailDto extends UserDetailDto {
    private String staffId;
    private String position;
    private String degree;
    private String bio;
    private String expertise;
    private float averageRating;
    private String departmentName;
    private Set<String> subjects; // Danh sách tên các môn học
}
