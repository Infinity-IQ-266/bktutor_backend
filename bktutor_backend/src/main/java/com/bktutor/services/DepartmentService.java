package com.bktutor.services;

import com.bktutor.common.dtos.DepartmentDto;
import com.bktutor.common.dtos.DepartmentSearchDto;
import org.springframework.data.domain.Page;

public interface DepartmentService {

    Page<DepartmentDto> searchDepartment(DepartmentSearchDto build);

    DepartmentDto findDepartmentById(Long departmentId);

    DepartmentDto createDepartment(DepartmentDto request);
}
