package com.github.oxyethylene.springboot4demo.entity.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String paymentMethod;
    private BigDecimal amount;
    private String currency;
    private String customerEmail;
    private String description;
}
