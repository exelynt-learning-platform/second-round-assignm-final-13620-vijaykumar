package com.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.dto.RegisterRequest;
import com.dto.RegisterResponse;
import com.dto.UserResponse;
import com.entity.User;
import com.exceptions.ResourceNotFoundException;
import com.repository.UserRepository;
import com.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserResponse getAuthenticatedUser() {

		log.debug("getAuthenticatedUser()");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
			throw new RuntimeException("User is not authenticated");
		}

		User userDetails = (User) authentication.getPrincipal();

		User currentUser = userRepository.findByEmail(userDetails.getEmail()).orElseThrow(
				() -> new ResourceNotFoundException("User not found with email: " + userDetails.getEmail()));

		log.info("User found with email: {}", currentUser.getEmail());

		ModelMapper map = new ModelMapper();
		UserResponse ur = map.map(currentUser, UserResponse.class);

		return ur;
	}

	@Override
	public RegisterResponse updateUser(RegisterRequest registerRequest) {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(email).
				orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		
		ModelMapper map = new ModelMapper();
		RegisterResponse rr = map.map(user, RegisterResponse.class);

		userRepository.save(user);

		return rr;
	}
	
	@Override
	public List<UserResponse> getAllUsers() {
		
		List<User> users = userRepository.findAll();
		
		ModelMapper map = new ModelMapper();
		
		List<UserResponse> ur = users.stream()
	            .map(user -> map.map(user, UserResponse.class))
	            .toList();

		
		return ur;
	}
	
	@Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        ModelMapper map = new ModelMapper();
		UserResponse ur = map.map(user, UserResponse.class);
		
        return ur;
    }


	@Override
	public void deleteUser(Long id) {
		
		User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    userRepository.delete(user);
		
	}

	
	
	

}
