package com.example.HederaStableCoin.controller;

import com.example.HederaStableCoin.model.dto.CreatePaymentDTO;
import com.example.HederaStableCoin.model.dto.PaymentResponseDTO;
import com.example.HederaStableCoin.service.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentResponseDTO> createPaymentIntent(@RequestBody CreatePaymentDTO dto) {
        try {
            PaymentResponseDTO response = stripeService.createPaymentIntent(dto);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponseDTO(null, "Error creating payment intent: " + e.getMessage()));
        }
    }
}
