package com.github.oxyethylene.springboot4demo.controller;

import com.github.oxyethylene.springboot4demo.entity.PaymentResult;
import com.github.oxyethylene.springboot4demo.entity.request.PaymentRequest;
import com.github.oxyethylene.springboot4demo.enums.PaymentMethod;
import com.github.oxyethylene.springboot4demo.service.PaymentProcessingStrategy;
import com.github.oxyethylene.springboot4demo.service.PaymentProcessingStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentProcessingStrategyFactory strategyFactory;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest request) {
        // Determine payment method from request
        PaymentMethod paymentMethod = PaymentMethod.fromString(request.getPaymentMethod());

        // Get the appropriate strategy
        PaymentProcessingStrategy strategy = strategyFactory.getStrategy(paymentMethod);

        // Execute method-specific payment processing
        PaymentResult result = strategy.processPayment(request);

        return ResponseEntity.ok(result);
    }
}
