package com.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.ProductRequest;
import com.dto.ProductResponse;
import com.entity.Product;
import com.exceptions.ResourceNotFoundException;
import com.repository.ProductRepository;
import com.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
	
	@Autowired
    private ProductRepository productRepository;
	
	@Override
    public ProductResponse createProduct(ProductRequest request) {

		log.debug("createProduct()");

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());

        productRepository.save(product);

        log.info("Product created: {}", product.getName());

        ModelMapper map = new ModelMapper();
        ProductResponse response = map.map(product, ProductResponse.class);

        return response;
    }
	
	@Override
    public List<ProductResponse> getAllProducts() {

        log.debug("getAllProducts()");

        List<Product> products = productRepository.findAll();

        ModelMapper map = new ModelMapper();

        List<ProductResponse> response = products.stream()
                .map(product -> map.map(product, ProductResponse.class))
                .toList();

        return response;
    }
	
	@Override
    public ProductResponse getProductById(Long id) {

        log.debug("getProductById() with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        ModelMapper map = new ModelMapper();
        ProductResponse response = map.map(product, ProductResponse.class);

        return response;
    }
	
	@Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        log.debug("updateProduct() with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());

        productRepository.save(product);

        log.info("Product updated: {}", product.getName());

        ModelMapper map = new ModelMapper();
        ProductResponse response = map.map(product, ProductResponse.class);

        return response;
    }
	
	 @Override
	    public void deleteProduct(Long id) {

	        log.debug("deleteProduct() with id: {}", id);

	        Product product = productRepository.findById(id)
	                .orElseThrow(() ->
	      new ResourceNotFoundException("Product not found with id: " + id));

	        productRepository.delete(product);

	        log.info("Product deleted with id: {}", id);
	    }
	
}
