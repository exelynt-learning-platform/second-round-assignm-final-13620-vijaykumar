package com.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.CartItemRequest;
import com.entity.Cart;
import com.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart APIs", description = "Cart management operations")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "Add to Cart")
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody CartItemRequest request) {
    	
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @Operation(summary = "Update Cart")
    @PutMapping("/update")
    public ResponseEntity<Cart> updateCart(@RequestBody CartItemRequest request) {
    	
        return ResponseEntity.ok(cartService.updateCart(request));
    }

    @Operation(summary = "Remove Item")
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<String> removeItem(@PathVariable Long itemId) {
    	
        cartService.removeItem(itemId);
        return ResponseEntity.ok("Item removed");
    }

    @Operation(summary = "View Cart")
    @GetMapping("/viewCart")
    public ResponseEntity<Cart> viewCart() {
    	
        return ResponseEntity.ok(cartService.viewCart());
    }

    @Operation(summary = "Clear Cart")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
    	
        cartService.clearCart();
        return ResponseEntity.ok("Cart cleared");
    }
}