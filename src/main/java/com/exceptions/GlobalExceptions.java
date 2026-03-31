package com.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dto.ErrorHandlingResponse;

@RestControllerAdvice
public class GlobalExceptions {

	@ExceptionHandler(ResourceFoundException.class)
	private ResponseEntity<ErrorHandlingResponse> handleAllErrors(ResourceFoundException re) {

		return new ResponseEntity<>(ErrorHandlingResponse.builder().msg(re.getMessage()).build(), HttpStatus.CONFLICT); 
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentValidException(MethodArgumentNotValidException msg) {

		Map<String, String> errors = new HashMap<>();

		List<FieldError> fieldErrors = msg.getBindingResult().getFieldErrors();

		for (FieldError e : fieldErrors) {
			errors.put(e.getField(), e.getDefaultMessage());
		}
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}
}
