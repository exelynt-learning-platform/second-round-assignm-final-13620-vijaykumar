package com.serviceimpl;

import java.time.Instant;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.dto.RegisterRequest;
import com.dto.RegisterResponse;
import com.entity.Role;
import com.entity.User;
import com.enums.RoleEnum;
import com.exceptions.ResourceFoundException;
import com.exceptions.ResourceNotFoundException;
import com.repository.RoleRepository;
import com.repository.UserRepository;
import com.security.CustomUserDetails;
import com.service.AuthService;
import com.service.JwtService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public RegisterResponse registerUser(@Valid RegisterRequest registerRequest) {
		
		log.info("Registered User Details" + registerRequest);
		
		userRepository.findByEmail(registerRequest.getEmail()).ifPresent((user) -> {
			throw new ResourceFoundException("User with this email already exists");
		});
		
		ModelMapper modal = new ModelMapper();
		User user = modal.map(registerRequest, User.class);
		
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		
		Role role = roleRepository.findByRolename(RoleEnum.USER)
		        .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
		
		user.setRole(role);
		
		User savedUser = userRepository.save(user);
		
		RegisterResponse rr = modal.map(savedUser, RegisterResponse.class);
		
		return rr;
	}

	@Override
	public AuthResponse userLogin(@Valid AuthRequest authRequest) {
		
		log.info("Login attempt for email: {}", authRequest.getEmail());
		
		authenticationManager.
		authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
				authRequest.getPassword()));
		
		User authenticatedUser = userRepository.findByEmail(authRequest.getEmail()).
				orElseThrow(() -> new UsernameNotFoundException(authRequest.getEmail()));
		
	    String jwtToken = jwtService.generateToken(authenticatedUser);

		Instant currentDate = Instant.now();
				
		return AuthResponse.builder().userId(authenticatedUser.getId().toString()).token(jwtToken).
				expiryAt(currentDate.plusMillis(jwtService.getExpirationTime()).toString()).build();
	}

	@Override
	public RegisterResponse createAdministrator(RegisterRequest registerRequest) {
		
		userRepository.findByEmail(registerRequest.getEmail()).ifPresent((user) -> {
			throw new ResourceFoundException("User with this email already exists");
		});
		
		Role role = roleRepository.findByRolename(RoleEnum.ADMIN).get();

		ModelMapper map = new ModelMapper();
		User user = map.map(registerRequest, User.class);
		
		user.setRole(role);
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));


		User savedUser = userRepository.save(user);

		RegisterResponse registerResponse = map.map(savedUser, RegisterResponse.class);

		return registerResponse;
	}


}
