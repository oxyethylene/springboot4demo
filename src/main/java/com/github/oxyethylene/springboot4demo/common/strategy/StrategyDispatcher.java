package com.github.oxyethylene.springboot4demo.common.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Dispatcher that routes strategy execution based on @StrategyMapping annotations.
 *
 * Usage in controllers:
 * <pre>
 * @RestController
 * @RequiredArgsConstructor
 * public class PaymentController {
 *     private final StrategyDispatcher dispatcher;
 *
 *     @PostMapping("/payment")
 *     public PaymentResult process(@RequestBody PaymentRequest request) {
 *         return dispatcher.dispatch(request.getPaymentMethod(), PaymentResult.class, request);
 *     }
 * }
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class StrategyDispatcher {

    private final StrategyMappingRegistry registry;

    /**
     * Dispatch to the appropriate strategy handler
     *
     * @param key the strategy key (will be converted to string)
     * @param resultType the expected result type
     * @param args arguments to pass to the strategy method
     * @return the result from the strategy method
     */
    @SuppressWarnings("unchecked")
    public <T> T dispatch(Object key, Class<T> resultType, Object... args) {
        String keyString = key.toString();
        Object result = registry.invoke(keyString, args);
        return (T) result;
    }

    /**
     * Dispatch to the appropriate strategy handler (without explicit result type)
     *
     * @param key the strategy key
     * @param args arguments to pass to the strategy method
     * @return the result from the strategy method
     */
    public Object dispatch(Object key, Object... args) {
        String keyString = key.toString();
        return registry.invoke(keyString, args);
    }

    /**
     * Check if a handler exists for the given key
     */
    public boolean hasHandler(Object key) {
        return registry.hasMapping(key.toString());
    }
}
