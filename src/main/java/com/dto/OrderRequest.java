package com.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
	
    @NotNull(message = "Shipping address is required")
    private String shippingAddress;
    
    private String paymentMethodId;
}
