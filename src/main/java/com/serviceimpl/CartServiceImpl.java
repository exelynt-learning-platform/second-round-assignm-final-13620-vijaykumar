package com.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dto.CartItemRequest;
import com.entity.Cart;
import com.entity.CartItem;
import com.entity.Product;
import com.entity.User;
import com.exceptions.ResourceNotFoundException;
import com.repository.CartItemRepository;
import com.repository.CartRepository;
import com.repository.ProductRepository;
import com.repository.UserRepository;
import com.service.CartService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ GET CURRENT USER
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // ✅ ADD TO CART
    @Override
    public Cart addToCart(CartItemRequest request) {

        log.debug("addToCart()");

        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<CartItem> existingItem =
                cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {

            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());

            // ✅ Always keep unit price
            item.setPrice(product.getPrice());

            cartItemRepository.save(item);

        } else {

            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());

            // ✅ FIX: store unit price only
            item.setPrice(product.getPrice());

            cartItemRepository.save(item);
        }

        updateTotal(cart);

        log.info("Product added to cart");

        return cart;
    }

    // ✅ UPDATE CART
    @Override
    public Cart updateCart(CartItemRequest request) {

        log.debug("updateCart()");

        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        item.setQuantity(request.getQuantity());

        // ✅ IMPORTANT: update unit price
        item.setPrice(product.getPrice());

        cartItemRepository.save(item);

        updateTotal(cart);

        return cart;
    }

    // ✅ REMOVE ITEM
    @Override
    public void removeItem(Long itemId) {

        log.debug("removeItem()");

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        Cart cart = item.getCart();

        cartItemRepository.delete(item);

        // ✅ IMPORTANT: recalculate total
        updateTotal(cart);

        log.info("Item removed from cart");
    }

    // ✅ VIEW CART
    @Override
    public Cart viewCart() {

        log.debug("viewCart()");

        User user = getCurrentUser();

        return cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    // ✅ CLEAR CART
    @Override
    public void clearCart() {

        log.debug("clearCart()");

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();
        cart.setTotalPrice(0.0);

        cartRepository.save(cart);

        log.info("Cart cleared");
    }

    // ✅ CALCULATE TOTAL
    private void updateTotal(Cart cart) {

        double total = cart.getItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        cart.setTotalPrice(total);

        cartRepository.save(cart);
    }
}