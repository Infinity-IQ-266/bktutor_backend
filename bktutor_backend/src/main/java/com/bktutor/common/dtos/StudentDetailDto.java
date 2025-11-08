package com.bktutor.common.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDetailDto extends UserDetailDto {
    private String studentId;
    private String major;
    private int academicYear;
    private String className;
    private String departmentName;
}
