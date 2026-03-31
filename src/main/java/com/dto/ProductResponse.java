package com.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {

	private Long id;
	
	private String name;
	
	private String description;
	
	private double price;
	
	private int stockQuantity;
	
	private String imageUrl;
	
}
