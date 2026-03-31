package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.RegisterRequest;
import com.dto.RegisterResponse;
import com.dto.UserResponse;
import com.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/users")
@Tag(name = "User APIS", description = "All APIS Are related to User")
public class UserController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Get Authenticated User", description = "Retrieves the details of the currently authenticated user.")
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/authenticatedUser")
	public ResponseEntity<UserResponse> getAuthenticatedUser() {

		log.debug("getAuthenticatedUser()");

		return ResponseEntity.ok(userService.getAuthenticatedUser());
	}

	@Operation(summary = "Update User", description = "Update logged-in user details")
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/updateUser")
	public ResponseEntity<RegisterResponse> updateUser(@Validated @RequestBody RegisterRequest registerRequest) {

		return ResponseEntity.ok(userService.updateUser(registerRequest));
	}

	@Operation(summary = "Get User by ID", description = "Fetch user by ID")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@Valid @PathVariable Long id) {
		
		return ResponseEntity.ok(userService.getUserById(id));
	}
	
	@Operation(summary = "Get All User", description = "Get All User tails.")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<UserResponse>> getAllUsers() {

		return ResponseEntity.ok(userService.getAllUsers());
	}

	@Operation(summary = "delere User by ID", description = "Selete the user.")
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {

		userService.deleteUser(id);

		return ResponseEntity.ok("User deleted successfully");
	}

	

}
