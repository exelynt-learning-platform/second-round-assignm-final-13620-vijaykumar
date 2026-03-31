package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Cart;
import com.entity.CartItem;
import com.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
	
	

}
