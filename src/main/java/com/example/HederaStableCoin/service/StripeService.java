package com.example.HederaStableCoin.service;

import com.example.HederaStableCoin.model.dto.CreatePaymentDTO;
import com.example.HederaStableCoin.model.dto.PaymentResponseDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    public PaymentResponseDTO createPaymentIntent(CreatePaymentDTO dto) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(dto.getAmount())
                .setCurrency(dto.getCurrency())
                .setReceiptEmail(dto.getEmail())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return new PaymentResponseDTO(paymentIntent.getClientSecret(), paymentIntent.getStatus());
    }
}
