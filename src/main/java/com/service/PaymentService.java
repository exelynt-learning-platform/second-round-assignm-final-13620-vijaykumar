package com.service;

import com.dto.PaymentRequest;

public interface PaymentService {

    String createPaymentSession(PaymentRequest request);

    String paymentSuccess(String sessionId);

    String paymentCancel();
}