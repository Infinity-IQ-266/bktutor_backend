package com.bktutor.controller;

import com.bktutor.response.Response;
import com.bktutor.services.StudentService;
import com.bktutor.utils.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public Response getStudentDashboard() {
        String studentUsername = SecurityUtil.getUsername();
        return new Response(studentService.getDashboardData(studentUsername));
    }
}
