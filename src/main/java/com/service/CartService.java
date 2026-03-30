package com.service;

import java.util.List;

import org.jspecify.annotations.Nullable;

import com.dto.CartItemRequest;
import com.entity.Cart;

public interface CartService {

	Cart addToCart(CartItemRequest request);

	Cart updateCart(CartItemRequest request);

	void removeItem(Long itemId);

	Cart viewCart();

	void clearCart();

}
