package com.bktutor.converter;

import com.bktutor.common.dtos.TutorDto;
import com.bktutor.common.entity.Tutor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("tutorConverter")
public class TutorConverter extends SuperConverter<TutorDto, Tutor> {

    private final ModelMapper modelMapper;

    public TutorConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TutorDto convertToDTO(Tutor entity) {
        TutorDto dto = modelMapper.map(entity, TutorDto.class);
        if (entity.getDepartment() != null) {
            dto.setDepartmentName(entity.getDepartment().getName());
        }
        return dto;
    }

    @Override
    public Tutor convertToEntity(TutorDto dto) {
        return modelMapper.map(dto, Tutor.class);
    }
}
