package com.bktutor.services;

import com.bktutor.common.dtos.AuthenticationResponse;
import com.bktutor.common.dtos.LoginRequest;
import com.bktutor.common.dtos.RegisterRequest;
import jakarta.validation.Valid;

public interface AuthenticationService {

    AuthenticationResponse registerUser(@Valid RegisterRequest request);
    AuthenticationResponse login(@Valid LoginRequest request);

}
