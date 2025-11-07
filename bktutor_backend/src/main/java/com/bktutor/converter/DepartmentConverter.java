package com.bktutor.converter;

import com.bktutor.common.dtos.DepartmentDto;
import com.bktutor.common.entity.Department;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("departmentConverter")
public class DepartmentConverter extends SuperConverter<DepartmentDto, Department> {

    private final ModelMapper modelMapper;

    public DepartmentConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentDto convertToDTO(Department entity) {
        return modelMapper.map(entity, DepartmentDto.class);
    }

    @Override
    public Department convertToEntity(DepartmentDto dto) {
        return modelMapper.map(dto, Department.class);
    }
}
