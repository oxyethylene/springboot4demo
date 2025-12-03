package com.github.oxyethylene.springboot4demo.common.strategy;

import java.lang.annotation.*;

/**
 * Annotation to mark a method as a strategy handler for a specific key.
 * Similar to @RequestMapping for REST endpoints.
 *
 * Usage:
 * <pre>
 * @StrategyHandler
 * public class PaymentStrategyHandler {
 *
 *     @StrategyMapping(PaymentMethod.CREDIT_CARD)
 *     public PaymentResult handleCreditCard(PaymentRequest request) {
 *         // Credit card logic
 *     }
 *
 *     @StrategyMapping(PaymentMethod.PAYPAL)
 *     public PaymentResult handlePayPal(PaymentRequest request) {
 *         // PayPal logic
 *     }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrategyMapping {
    /**
     * The strategy key(s) that this method handles.
     * Can be enum values, strings, or any other key type.
     */
    String[] value();
}
