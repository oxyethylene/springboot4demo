package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyFactory;
import com.github.oxyethylene.springboot4demo.enums.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessingStrategyFactory extends StrategyFactory<PaymentMethod, PaymentProcessingStrategy> {
}
