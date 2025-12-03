package com.github.oxyethylene.springboot4demo.controller;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyDispatcher;
import com.github.oxyethylene.springboot4demo.entity.PaymentResult;
import com.github.oxyethylene.springboot4demo.entity.request.PaymentRequest;
import com.github.oxyethylene.springboot4demo.enums.PaymentMethod;
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

    private final StrategyDispatcher dispatcher;

    /**
     * Process payment using annotation-based strategy routing.
     * Just dispatch - Spring handles the routing automatically!
     */
    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest request) {
        PaymentMethod paymentMethod = PaymentMethod.fromString(request.getPaymentMethod());

        // Dispatch automatically routes to the correct @StrategyMapping method
        PaymentResult result = dispatcher.dispatch(paymentMethod, PaymentResult.class, request);

        return ResponseEntity.ok(result);
    }
}
