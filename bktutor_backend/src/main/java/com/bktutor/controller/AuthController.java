package com.bktutor.controller;


import com.bktutor.common.dtos.LoginRequest;
import com.bktutor.common.dtos.RegisterRequest;
import com.bktutor.response.Response;
import com.bktutor.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@Validated
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public Response registerUser(@Valid @RequestBody RegisterRequest request) {
        return new Response(authenticationService.registerUser(request));
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequest request) {
        return new Response(authenticationService.login(request));
    }
}
