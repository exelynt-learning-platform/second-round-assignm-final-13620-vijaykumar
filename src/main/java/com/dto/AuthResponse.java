package com.dto;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Validated
public class AuthResponse {

	private String userId;
	
	private String token;
	
	private String expiryAt;
	

}
