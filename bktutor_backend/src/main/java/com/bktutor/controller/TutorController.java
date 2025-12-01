package com.bktutor.controller;

import com.bktutor.common.dtos.TutorSearchDto;
import com.bktutor.common.enums.DirectionEnum;
import com.bktutor.response.Response;
import com.bktutor.services.TutorService;
import com.bktutor.utils.SecurityUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tutors")
public class TutorController {
    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping
    public Response getAllTutors(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "direction", required = false, defaultValue = "DESC") DirectionEnum direction,
            @RequestParam(name = "attribute", required = false, defaultValue = "id") String attribute,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "department", required = false) String department
    ) {
        TutorSearchDto searchDto = TutorSearchDto.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .attribute(attribute)
                .name(name)
                .department(department)
                .build();
        return new Response(tutorService.searchTutors(searchDto));
    }
    @GetMapping("{tutorId}")
    public Response getTutorById(@PathVariable Long tutorId) {
        return new Response(tutorService.findTutorById(tutorId));
    }

    @GetMapping("/dashboard")
    public Response getTutorDashboard(){
        String username = SecurityUtil.getUsername();
        return new Response(tutorService.getDashboardData(username));
    }

    @GetMapping("/me/students")
    public Response getMyStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String username = SecurityUtil.getUsername();
        Pageable pageable = PageRequest.of(page, size);
        return new Response(tutorService.getMyStudents(username, pageable));
    }
}
