package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyHandler;
import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.PaymentResult;
import com.github.oxyethylene.springboot4demo.entity.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Annotation-based payment processing handler.
 * All payment methods in one class with @StrategyMapping annotations.
 */
@Slf4j
@StrategyHandler
public class PaymentProcessingHandler {

    @StrategyMapping("CREDIT_CARD")
    public PaymentResult handleCreditCard(PaymentRequest request) {
        log.info("Processing credit card payment for amount: {} {}",
                request.getAmount(), request.getCurrency());

        // Credit card specific logic
        PaymentResult result = new PaymentResult();
        result.setTransactionId("CC-" + UUID.randomUUID().toString());
        result.setStatus("SUCCESS");
        result.setAmount(request.getAmount());
        result.setCurrency(request.getCurrency());
        result.setProcessedAt(LocalDateTime.now());
        result.setMessage("Credit card payment processed successfully");

        return result;
    }

    @StrategyMapping("PAYPAL")
    public PaymentResult handlePayPal(PaymentRequest request) {
        log.info("Processing PayPal payment for amount: {} {}",
                request.getAmount(), request.getCurrency());

        // PayPal specific logic
        PaymentResult result = new PaymentResult();
        result.setTransactionId("PP-" + UUID.randomUUID().toString());
        result.setStatus("SUCCESS");
        result.setAmount(request.getAmount());
        result.setCurrency(request.getCurrency());
        result.setProcessedAt(LocalDateTime.now());
        result.setMessage("PayPal payment processed successfully");

        return result;
    }

    @StrategyMapping("BANK_TRANSFER")
    public PaymentResult handleBankTransfer(PaymentRequest request) {
        log.info("Processing bank transfer for amount: {} {}",
                request.getAmount(), request.getCurrency());

        // Bank transfer specific logic
        PaymentResult result = new PaymentResult();
        result.setTransactionId("BT-" + UUID.randomUUID().toString());
        result.setStatus("PENDING");
        result.setAmount(request.getAmount());
        result.setCurrency(request.getCurrency());
        result.setProcessedAt(LocalDateTime.now());
        result.setMessage("Bank transfer initiated, processing may take 1-3 business days");

        return result;
    }

    @StrategyMapping("CRYPTO")
    public PaymentResult handleCrypto(PaymentRequest request) {
        log.info("Processing crypto payment for amount: {} {}",
                request.getAmount(), request.getCurrency());

        // Crypto specific logic
        PaymentResult result = new PaymentResult();
        result.setTransactionId("CRYPTO-" + UUID.randomUUID().toString());
        result.setStatus("SUCCESS");
        result.setAmount(request.getAmount());
        result.setCurrency(request.getCurrency());
        result.setProcessedAt(LocalDateTime.now());
        result.setMessage("Cryptocurrency payment processed successfully");

        return result;
    }
}
