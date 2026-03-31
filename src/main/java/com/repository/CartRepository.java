package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Cart;
import com.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUser(User user);

}
