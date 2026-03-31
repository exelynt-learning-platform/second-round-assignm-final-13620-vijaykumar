package com.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.OrderRequest;
import com.dto.OrderResponse;
import com.entity.*;
import com.enums.OrderStatus;
import com.enums.PaymentStatus;
import com.exceptions.ResourceNotFoundException;
import com.repository.*;
import com.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	private User getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.debug("createOrder() started");

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderStatus(OrderStatus.PENDING);

        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Product " + product.getName() + " is out of stock");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());

            total += cartItem.getPrice() * cartItem.getQuantity();

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(savedOrder.getId());
        orderResponse.setTotalAmount(savedOrder.getTotalAmount());
        orderResponse.setShippingAddress(savedOrder.getShippingAddress());
        orderResponse.setPaymentStatus(savedOrder.getPaymentStatus());
        orderResponse.setOrderStatus(savedOrder.getOrderStatus());

        log.info("Order created with ID: {}", savedOrder.getId());
        
        return orderResponse;
    }

	@Override
	public List<OrderResponse> getUserOrders() {

		log.debug("getUserOrders()");

		User user = getCurrentUser();

		List<Order> orders = orderRepository.findByUser(user);

		ModelMapper map = new ModelMapper();

		List<OrderResponse> response = orders.stream()
				.map(order -> map.map(order, OrderResponse.class))
				.toList();

		return response;
	}

	@Override
	public OrderResponse getOrderById(Long id) {

		log.debug("getOrderById()");

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		ModelMapper map = new ModelMapper();

		return map.map(order, OrderResponse.class);
	}

	@Override
	public List<OrderResponse> getAllOrders() {

		log.debug("getAllOrders()");

		List<Order> orders = orderRepository.findAll();

		ModelMapper map = new ModelMapper();

		List<OrderResponse> response = orders.stream()
				.map(order -> map.map(order, OrderResponse.class))
				.toList();

		return response;
	}

	@Override
	public OrderResponse updateOrderStatus(Long id, OrderStatus status) {

		log.debug("updateOrderStatus()");

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		order.setOrderStatus(status);

		orderRepository.save(order);

		ModelMapper map = new ModelMapper();
		OrderResponse orderResponse = map.map(order, OrderResponse.class);

		log.info("Order status updated: {}", status);

		return orderResponse;
	}
}