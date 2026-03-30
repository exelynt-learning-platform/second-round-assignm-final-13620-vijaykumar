package com.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Enumerated(EnumType.STRING)
	@Column(unique = true, nullable = false)
	private RoleEnum rolename;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "role")
	@ToString.Exclude 
	@JsonIgnore
	private List<User> user; 
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@CreationTimestamp
	@Column(name = "created_At", nullable = false)
	private Instant createdAt;
	
	@CreationTimestamp
	@Column(name = "updated_At", nullable = false)
	private Instant updatedAt;


}
