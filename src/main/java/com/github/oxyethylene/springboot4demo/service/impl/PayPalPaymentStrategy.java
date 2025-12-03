package com.github.oxyethylene.springboot4demo.service.impl;

import com.github.oxyethylene.springboot4demo.entity.PaymentResult;
import com.github.oxyethylene.springboot4demo.entity.request.PaymentRequest;
import com.github.oxyethylene.springboot4demo.enums.PaymentMethod;
import com.github.oxyethylene.springboot4demo.service.PaymentProcessingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class PayPalPaymentStrategy implements PaymentProcessingStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.PAYPAL;
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        validate(request);

        log.info("Processing PayPal payment for amount: {} {}",
                request.getAmount(), request.getCurrency());

        // PayPal specific logic:
        // - Redirect to PayPal authorization
        // - Handle OAuth flow
        // - Process PayPal API response

        PaymentResult result = new PaymentResult();
        result.setTransactionId("PP-" + UUID.randomUUID().toString());
        result.setStatus("SUCCESS");
        result.setAmount(request.getAmount());
        result.setCurrency(request.getCurrency());
        result.setProcessedAt(LocalDateTime.now());
        result.setMessage("PayPal payment processed successfully");

        return result;
    }
}
