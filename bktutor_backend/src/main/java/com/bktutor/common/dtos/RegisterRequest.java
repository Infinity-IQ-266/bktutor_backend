package com.bktutor.common.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "Username must contain only letters, numbers, underscores and be 3–20 characters long.")
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

}

