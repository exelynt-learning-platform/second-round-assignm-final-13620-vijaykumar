package com.service;

import java.util.Map;
import java.util.function.Function;

import com.security.CustomUserDetails;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
	
	String extractUsername(String token);

	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	String generateToken(CustomUserDetails userDetails);

	String generateToken(Map<String, Object> extraClaims, CustomUserDetails userDetails);

	long getExpirationTime();

	boolean isTokenValid(String token, CustomUserDetails userDetails);

	String extractTokenFromRequest(HttpServletRequest request);

}
