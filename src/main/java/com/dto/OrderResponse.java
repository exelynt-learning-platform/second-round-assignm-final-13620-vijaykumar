package com.dto;


import com.enums.OrderStatus;
import com.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    
    private Double totalAmount;
    
    private String shippingAddress;
    
    @Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;
    
    @Enumerated(EnumType.STRING)
	@Column(nullable = false)
    private PaymentStatus paymentStatus;
}