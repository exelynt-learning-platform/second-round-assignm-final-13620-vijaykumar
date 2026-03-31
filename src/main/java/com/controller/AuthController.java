package com.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.dto.RegisterRequest;
import com.dto.RegisterResponse;
import com.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Authentication APIS", description = "All APIS Are related to Authentication")
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
	@PostMapping("/registerUser")
	public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

		log.debug("Register Request: " + registerRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(registerRequest));
	}

	@Operation(summary = "Login existing user", description = "Login existing user with details.")
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> userLogin(@Valid @RequestBody AuthRequest authRequest) {

		log.debug("Auth Request: " + authRequest);

		return ResponseEntity.status(HttpStatus.OK).body(authService.userLogin(authRequest));

	}

	@Operation(summary = "Create an administrator", description = "Creates a new administrator user. Requires SUPER_ADMIN role.")
	@PostMapping("/createAdministrator")
	public ResponseEntity<RegisterResponse> createAdministrator(@RequestBody @Valid RegisterRequest registerRequest) {
		
		log.debug("createAdministrator({})", registerRequest.getEmail());
		
		return ResponseEntity.status(HttpStatus.OK).body(authService.createAdministrator(registerRequest));

	}

}
