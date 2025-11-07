package com.bktutor.services.impl;

import com.bktutor.common.dtos.TutorDto;
import com.bktutor.common.dtos.TutorSearchDto;
import com.bktutor.common.entity.Tutor;
import com.bktutor.common.enums.ErrorMessage;
import com.bktutor.converter.TutorConverter;
import com.bktutor.exception.NotFoundException;
import com.bktutor.repository.TutorRepository;
import com.bktutor.services.TutorService;
import com.bktutor.specification.TutorSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final TutorConverter tutorConverter;

    public TutorServiceImpl(TutorRepository tutorRepository, TutorConverter tutorConverter) {
        this.tutorRepository = tutorRepository;
        this.tutorConverter = tutorConverter;
    }

    @Override
    public Page<TutorDto> searchTutors(TutorSearchDto searchDto) {
        Specification<Tutor> specification = TutorSpecification.hasNameLike(searchDto.getName())
                .and(TutorSpecification.inDepartment(searchDto.getDepartment()));
        Sort sortable = Sort.by(Sort.Direction.fromString(searchDto.getDirection().name()), searchDto.getAttribute());

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sortable);

        Page<Tutor> tutors = tutorRepository.findAll(specification, pageable);

        return tutors.map(tutorConverter::convertToDTO);
    }

    @Override
    public TutorDto findTutorById(Long tutorId) {
        Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new NotFoundException(ErrorMessage.TUTOR_NOT_FOUND, "Tutor not found"));
        return tutorConverter.convertToDTO(tutor);
    }
}
