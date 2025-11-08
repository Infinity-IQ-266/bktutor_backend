package com.bktutor.services;

import com.bktutor.common.dtos.StudentDashboardDto;

public interface StudentService {
    StudentDashboardDto getDashboardData(String studentUsername);
}
