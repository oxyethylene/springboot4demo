package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.Strategy;
import com.github.oxyethylene.springboot4demo.entity.PaymentResult;
import com.github.oxyethylene.springboot4demo.entity.request.PaymentRequest;
import com.github.oxyethylene.springboot4demo.enums.PaymentMethod;

/**
 * Strategy interface for payment processing
 * Example of reusing the generic strategy pattern for a different domain
 */
public interface PaymentProcessingStrategy extends Strategy<PaymentMethod> {

    @Override
    default PaymentMethod getStrategyKey() {
        return getPaymentMethod();
    }

    /**
     * Get the payment method this strategy handles
     */
    PaymentMethod getPaymentMethod();

    /**
     * Process payment with method-specific logic
     */
    PaymentResult processPayment(PaymentRequest request);

    /**
     * Validate payment request before processing
     */
    default void validate(PaymentRequest request) {
        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }
    }
}
