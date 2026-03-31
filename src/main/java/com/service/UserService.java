package com.service;

import java.util.List;

import com.dto.RegisterRequest;
import com.dto.RegisterResponse;
import com.dto.UserResponse;

public interface UserService {

	UserResponse getAuthenticatedUser();

	RegisterResponse updateUser(RegisterRequest registerRequest);

	void deleteUser(Long id);

	List<UserResponse> getAllUsers();

	UserResponse getUserById(Long id);

}
