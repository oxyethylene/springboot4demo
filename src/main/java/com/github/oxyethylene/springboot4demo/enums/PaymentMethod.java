package com.github.oxyethylene.springboot4demo.enums;

public enum PaymentMethod {
    CREDIT_CARD,
    PAYPAL,
    BANK_TRANSFER,
    CRYPTO;

    public static PaymentMethod fromString(String method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }

        try {
            return PaymentMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown payment method: " + method);
        }
    }
}
