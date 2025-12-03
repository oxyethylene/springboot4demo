package com.github.oxyethylene.springboot4demo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResult {
    private String transactionId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime processedAt;
    private String message;
}
