package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Order;
import com.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long>{

	List<Order> findByUser(User user);

}
