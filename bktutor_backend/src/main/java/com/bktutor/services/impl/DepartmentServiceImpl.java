package com.bktutor.services.impl;

import com.bktutor.common.dtos.DepartmentDto;
import com.bktutor.common.dtos.DepartmentSearchDto;
import com.bktutor.common.entity.Department;
import com.bktutor.common.entity.User;
import com.bktutor.common.enums.ErrorMessage;
import com.bktutor.common.enums.Role;
import com.bktutor.converter.DepartmentConverter;
import com.bktutor.exception.ConflictException;
import com.bktutor.exception.ForbiddenException;
import com.bktutor.exception.NotFoundException;
import com.bktutor.repository.DepartmentRepository;
import com.bktutor.repository.UserRepository;
import com.bktutor.services.DepartmentService;
import com.bktutor.specification.DepartmentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter departmentConverter;
    private final UserRepository userRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentConverter departmentConverter, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
        this.userRepository = userRepository;
    }

    @Override
    public Page<DepartmentDto> searchDepartment(DepartmentSearchDto request) {
        Specification<Department> specification = DepartmentSpecification.searchByName(request.getName());

        Sort sortable = Sort.by(Sort.Direction.fromString(request.getDirection().name()), request.getAttribute());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortable);

        Page<Department> departments = departmentRepository.findAll(specification, pageable);

        return departments.map(departmentConverter::convertToDTO);
    }

    @Override
    public DepartmentDto findDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new NotFoundException(ErrorMessage.DEPARTMENT_NOT_FOUND, "Department not found"));
        return departmentConverter.convertToDTO(department);
    }

    @Override
    @PreAuthorize("hasRole('PROGRAM_ADMINISTRATOR')")
    public DepartmentDto createDepartment(DepartmentDto request) {
        departmentRepository.findByName(request.getName()).ifPresent(d -> {
            throw new ConflictException(ErrorMessage.USERNAME_ALREADY_EXISTS, "Department name already exists");
        });

        Department department = departmentConverter.convertToEntity(request);
        departmentRepository.save(department);
        return departmentConverter.convertToDTO(department);
    }
}
