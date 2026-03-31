package com.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dto.PaymentRequest;
import com.entity.Order;
import com.enums.PaymentStatus;
import com.exceptions.ResourceNotFoundException;
import com.repository.OrderRepository;
import com.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    @Override
    public String createPaymentSession(PaymentRequest request) {

        log.debug("createPaymentSession()");

        Stripe.apiKey = secretKey;

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        try {
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                            .setCancelUrl(cancelUrl)

                            //STORE ORDER ID
                            .putMetadata("orderId", order.getId().toString())

                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("inr")
                                                            .setUnitAmount((long) (order.getTotalAmount() * 100))
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("Order #" + order.getId())
                                                                            .build()
                                                            )
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();

            Session session = Session.create(params);

            log.info("Stripe session created: {}", session.getId());

            return session.getUrl();

        } catch (Exception e) {
            log.error("Stripe session creation failed", e);
            throw new RuntimeException("Error creating payment session");
        }
    }

    @Override
    public String paymentSuccess(String sessionId) {

        log.debug("paymentSuccess()");

        Session session;

        try {
            session = Session.retrieve(sessionId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid or expired payment session");
        }

        String orderIdStr = session.getMetadata().get("orderId");

        if (orderIdStr == null) {
            throw new RuntimeException("Order ID missing in Stripe session");
        }

        Long orderId = Long.parseLong(orderIdStr);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            return "Order already paid";
        }

        order.setPaymentStatus(PaymentStatus.PAID);
        order.setPaymentId(sessionId);

        orderRepository.save(order);

        log.info("Payment successful for order: {}", orderId);

        return "Payment successful";
    }

    @Override
    public String paymentCancel() {

        log.debug("paymentCancel()");

        return "Payment cancelled";
    }
}