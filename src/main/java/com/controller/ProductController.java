package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.ProductRequest;
import com.dto.ProductResponse;
import com.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@Slf4j
@RequestMapping("/api/products")
@Tag(name = "Product APIs", description = "Operations related to product management")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Operation(summary = "Create Product", description = "Create a new product (ADMIN only)")
	@PostMapping("/createProduct")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {

		log.info("hello");
		return ResponseEntity.ok(productService.createProduct(request));

	}

	@Operation(summary = "Get All Products", description = "Retrieve all available products")
	@GetMapping("/getAllProducts")
	public ResponseEntity<List<ProductResponse>> getAllProducts() {

		return ResponseEntity.ok(productService.getAllProducts());
	}

	@Operation(summary = "Get Product by ID", description = "Retrieve product details by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {

		return ResponseEntity.ok(productService.getProductById(id));
	}

	@Operation(summary = "Update Product", description = "Update product details (ADMIN only)")
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductRequest request) {

		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	@Operation(summary = "Delete Product", description = "Delete product by ID (ADMIN only)")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {

		productService.deleteProduct(id);
		return ResponseEntity.ok("Product deleted successfully");
	}

}
