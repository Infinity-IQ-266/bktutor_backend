package com.bktutor.services;

import com.bktutor.common.dtos.StudentDetailDto;
import com.bktutor.common.dtos.TutorDashboardDto;
import com.bktutor.common.dtos.TutorDto;
import com.bktutor.common.dtos.TutorSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorService {
    Page<TutorDto> searchTutors(TutorSearchDto searchDto);

    TutorDto findTutorById(Long tutorId);

    TutorDashboardDto getDashboardData(String username);

    Page<StudentDetailDto> getMyStudents(String username, Pageable pageable);
}
