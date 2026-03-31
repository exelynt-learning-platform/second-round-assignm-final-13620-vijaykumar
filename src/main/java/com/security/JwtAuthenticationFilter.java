package com.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.entity.User;
import com.repository.UserRepository;
import com.service.JwtService;
import com.service.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private final JwtService jwtService;

	@Autowired
	private final UserDetailsService userDetailsService;
	
	@Autowired
    private final TokenBlacklistService blacklistService;
	
	@Autowired
	private final HandlerExceptionResolver handlerExceptionResolver;

	// Create USer -> Bearer Token Request ->
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
		final String authHeader = request.getHeader("Authorization");

		log.debug("AuthHeader = {}", authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			log.debug("Authentication header : {}", authHeader);
			log.warn("Authentication header is null/non-bearer");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7); // extract tocken
			final String userEmail = jwtService.extractUsername(jwt); // extract main basic username through tocken

			log.debug("UserEmail (from JWT) = {}", userEmail);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Tocken is saved
																									// here on server
																									// side
		//    blacklistService.isTokenBlacklisted(userEmail);
			if (userEmail != null && authentication == null) {
				log.info("No existing authentication header");
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

				boolean valid = jwtService.isTokenValid(jwt, (CustomUserDetails) userDetails);
				log.debug("Is Token Valid = {}", jwtService.isTokenValid(jwt, (CustomUserDetails) userDetails));

				if (valid) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					log.warn("Token has expired");
					User user = userRepository.findByEmail(userEmail)
							.orElseThrow(() -> new UsernameNotFoundException(userEmail));
					// user.setBlacklisted(true);
					userRepository.save(user);
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			log.error(exception.getMessage());
			handlerExceptionResolver.resolveException(request, response, null, exception);
		}
	}

}
