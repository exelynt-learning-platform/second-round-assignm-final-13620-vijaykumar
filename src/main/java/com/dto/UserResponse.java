package com.dto;

import org.springframework.validation.annotation.Validated;

import com.entity.Role;
import com.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Validated
public class UserResponse {
	
	private Long id;
	
    private String firstName;
    
    private String lastName;
    
    private String email;
    
    private String password;
    
	private Role role;


}
