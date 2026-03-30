package com.service;

import java.util.List;

import org.jspecify.annotations.Nullable;

import com.dto.ProductRequest;
import com.dto.ProductResponse;

import jakarta.validation.Valid;

public interface ProductService {

	ProductResponse createProduct(@Valid ProductRequest request);

	List<ProductResponse> getAllProducts();

	ProductResponse getProductById(Long id);

	ProductResponse updateProduct(Long id, @Valid ProductRequest request);

	void deleteProduct(Long id);

}
