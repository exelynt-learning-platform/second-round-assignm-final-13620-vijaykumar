package com.service;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.dto.RegisterRequest;
import com.dto.RegisterResponse;

import jakarta.validation.Valid;

public interface AuthService {

	RegisterResponse registerUser(@Valid RegisterRequest registerRequest);

	AuthResponse userLogin(@Valid AuthRequest authRequest);

	RegisterResponse createAdministrator(RegisterRequest registerRequest);

}
