package com.bktutor.services;

import com.bktutor.common.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorService {
    Page<TutorDto> searchTutors(TutorSearchDto searchDto);

    TutorDto findTutorById(Long tutorId);

    TutorDashboardDto getDashboardData(String username);

    Page<StudentDetailDto> getMyStudents(String username, Pageable pageable);

    Page<FeedbackDto> getMyFeedbacks(String username, Pageable pageable);
}
