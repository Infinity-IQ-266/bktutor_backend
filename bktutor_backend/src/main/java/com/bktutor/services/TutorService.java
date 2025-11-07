package com.bktutor.services;

import com.bktutor.common.dtos.TutorDto;
import com.bktutor.common.dtos.TutorSearchDto;
import org.springframework.data.domain.Page;

public interface TutorService {
    Page<TutorDto> searchTutors(TutorSearchDto searchDto);

    TutorDto findTutorById(Long tutorId);
}
