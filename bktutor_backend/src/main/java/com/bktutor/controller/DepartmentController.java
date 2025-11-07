package com.bktutor.controller;

import com.bktutor.common.dtos.DepartmentDto;
import com.bktutor.common.dtos.DepartmentSearchDto;
import com.bktutor.common.enums.DirectionEnum;
import com.bktutor.response.Response;
import com.bktutor.services.DepartmentService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping()
    public Response getAllDepartments(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") DirectionEnum direction,
            @RequestParam(name = "attribute", required = false, defaultValue = "createdAt") String attribute,
            @RequestParam(name = "name", required = false) String name
    ) {
        return new Response(departmentService.searchDepartment(
                DepartmentSearchDto
                        .builder()
                        .page(page)
                        .size(size)
                        .direction(direction)
                        .attribute(attribute)
                        .name(name)
                        .build()
        ));
    }

    @GetMapping("/{departmentId}")
    public Response getDepartmentById(@PathVariable Long departmentId) {
        return new Response(departmentService.findDepartmentById(departmentId));
    }

    @PostMapping()
    public Response createDepartment(@RequestBody DepartmentDto request) {
        return new Response(departmentService.createDepartment(request));
    }

}
