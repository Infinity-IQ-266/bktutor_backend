package com.bktutor.services.impl;


import com.bktutor.common.dtos.AuthenticationResponse;
import com.bktutor.common.dtos.LoginRequest;
import com.bktutor.common.dtos.RegisterRequest;
import com.bktutor.common.entity.Student;
import com.bktutor.common.entity.Tutor;
import com.bktutor.common.entity.User;
import com.bktutor.common.enums.ErrorMessage;
import com.bktutor.common.enums.Role;
import com.bktutor.exception.BadRequestException;
import com.bktutor.exception.NotFoundException;
import com.bktutor.repository.UserRepository;
import com.bktutor.services.AuthenticationService;
import com.bktutor.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse registerUser(@Valid RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException(ErrorMessage.USERNAME_ALREADY_EXISTS);
        }

        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setEmail(request.getEmail());
        student.setRole(Role.STUDENT);

        userRepository.save(student);

        return AuthenticationResponse.builder()
                .message("Register successful")
                .userId(student.getId())
                .build();
    }


    public AuthenticationResponse login(@Valid LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadRequestException(ErrorMessage.INVALID_USERNAME_PASSWORD);
        }

        String token = jwtService.generateToken(user.getUsername());

        String departmentName = null;
        if (user instanceof Student student) {
            if (student.getDepartment() != null) {
                departmentName = student.getDepartment().getName();
            }
        } else if (user instanceof Tutor tutor) {
            if (tutor.getDepartment() != null) {
                departmentName = tutor.getDepartment().getName();
            }
        }

        return AuthenticationResponse.builder()
                .message("Login successful")
                .token(token)
                .userId(user.getId())
                .fullName(user.getFullName())
                .faculty(departmentName)
                .build();
    }
}
