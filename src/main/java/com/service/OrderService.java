package com.service;

import java.util.List;

import com.dto.OrderRequest;
import com.dto.OrderResponse;
import com.enums.OrderStatus;

public interface OrderService {

	OrderResponse createOrder(OrderRequest request);

	List<OrderResponse> getUserOrders();

	List<OrderResponse> getAllOrders();

	OrderResponse getOrderById(Long id);

	OrderResponse updateOrderStatus(Long id, OrderStatus status);

}
