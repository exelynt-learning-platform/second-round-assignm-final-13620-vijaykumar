package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dto.OrderRequest;
import com.dto.OrderResponse;
import com.entity.Order;
import com.enums.OrderStatus;
import com.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order APIs", description = "Order management operations")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Create Order", description = "Create order from cart")
    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
       
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(orderService.createOrder(request));
    }

    @Operation(summary = "Get User Orders", description = "Get logged-in user orders")
    @GetMapping("/getUserOrders")
    public ResponseEntity<List<OrderResponse>> getUserOrders() {
    	
        return ResponseEntity.status(HttpStatus.OK)
        		.body(orderService.getUserOrders());
    }

    @Operation(summary = "Get Order by ID", description = "Fetch order details")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Get All Orders", description = "ADMIN: fetch all orders")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK)
        		.body(orderService.getAllOrders());
    }

    @Operation(summary = "Update Order Status", description = "ADMIN: update order status")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}