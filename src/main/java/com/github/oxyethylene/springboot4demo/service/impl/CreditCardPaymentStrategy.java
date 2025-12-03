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
public class CreditCardPaymentStrategy implements PaymentProcessingStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        validate(request);

        log.info("Processing credit card payment for amount: {} {}",
                request.getAmount(), request.getCurrency());

        // Credit card specific logic:
        // - Validate card number
        // - Check CVV
        // - Perform 3D Secure authentication
        // - Contact payment gateway

        PaymentResult result = new PaymentResult();
        result.setTransactionId("CC-" + UUID.randomUUID().toString());
        result.setStatus("SUCCESS");
        result.setAmount(request.getAmount());
        result.setCurrency(request.getCurrency());
        result.setProcessedAt(LocalDateTime.now());
        result.setMessage("Credit card payment processed successfully");

        return result;
    }
}
