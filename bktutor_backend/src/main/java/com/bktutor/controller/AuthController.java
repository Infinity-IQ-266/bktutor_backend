package com.bktutor.controller;


import com.bktutor.common.dtos.LoginRequest;
import com.bktutor.common.dtos.RegisterRequest;
import com.bktutor.response.Response;
import com.bktutor.services.AuthenticationService;
import com.bktutor.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public Response getMyInfo() {
        String username = SecurityUtil.getUsername();
        return new Response(authenticationService.getMe(username));
    }
}
