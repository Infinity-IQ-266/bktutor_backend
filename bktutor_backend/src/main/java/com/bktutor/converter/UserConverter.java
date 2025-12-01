package com.bktutor.converter;

import com.bktutor.common.dtos.StudentDetailDto;
import com.bktutor.common.dtos.TutorDetailDto;
import com.bktutor.common.dtos.UserDetailDto;
import com.bktutor.common.entity.Student;
import com.bktutor.common.entity.Subject;
import com.bktutor.common.entity.Tutor;
import com.bktutor.common.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("userConverter")
public class UserConverter extends SuperConverter<UserDetailDto, User> {

    private final ModelMapper modelMapper;

    public UserConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public UserDetailDto convertToDTO(User entity) {
        if (entity instanceof Student) {
            return convertToStudentDto((Student) entity);
        }
        if (entity instanceof Tutor) {
            return convertToTutorDto((Tutor) entity);
        }
        return modelMapper.map(entity, UserDetailDto.class);
    }

    private StudentDetailDto convertToStudentDto(Student student) {
        StudentDetailDto dto = modelMapper.map(student, StudentDetailDto.class);
        if (student.getDepartment() != null) {
            dto.setDepartmentName(student.getDepartment().getName());
        }
        if (student.getStudentId() != null) {
            dto.setStudentId(student.getStudentId());
        }
        if (student.getMajor() != null) {
            dto.setMajor(student.getMajor());
        }
        if (student.getClassName() != null) {
            dto.setClassName(student.getClassName());
        }
        dto.setAcademicYear(student.getAcademicYear());
        return dto;
    }

    private TutorDetailDto convertToTutorDto(Tutor tutor) {
        TutorDetailDto dto = modelMapper.map(tutor, TutorDetailDto.class);
        if (tutor.getDepartment() != null) {
            dto.setDepartmentName(tutor.getDepartment().getName());
        }
        if (tutor.getSubjects() != null) {
            dto.setSubjects(tutor.getSubjects().stream()
                    .map(Subject::getName)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }


    @Override
    public User convertToEntity(UserDetailDto dto) {
        return modelMapper.map(dto, User.class);
    }
}
