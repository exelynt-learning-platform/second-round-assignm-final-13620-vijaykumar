package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.PaymentRequest;
import com.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment APIs", description = "Stripe payment operations")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Create Payment Session")
    @PostMapping("/create-session")
    public ResponseEntity<String> createSession(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPaymentSession(request));
    }

    @Operation(summary = "Payment Success Callback")
    @GetMapping("/success")
    public ResponseEntity<String> success(@RequestParam String session_id) {
        return ResponseEntity.ok(paymentService.paymentSuccess(session_id));
    }

    // ✅ CANCEL
    @Operation(summary = "Payment Cancel")
    @GetMapping("/cancel")
    public ResponseEntity<String> cancel() {
        return ResponseEntity.ok(paymentService.paymentCancel());
    }
}