package com.github.oxyethylene.springboot4demo.common.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Dynamic proxy that routes method calls to the appropriate strategy implementation
 * Based on the current context or method parameters
 */
@Slf4j
@RequiredArgsConstructor
public class StrategyProxy<T> implements InvocationHandler {

    private final Class<T> interfaceClass;
    private final StrategyMappingRegistry registry;
    private final String[] contextKeys;

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceClass,
                               StrategyMappingRegistry registry,
                               String... contextKeys) {
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[]{interfaceClass},
            new StrategyProxy<>(interfaceClass, registry, contextKeys)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Get routing key from context
        CompositeKey key = getRoutingKey(method, args);

        // Get the actual strategy implementation
        T strategy = registry.getStrategy(interfaceClass, key);

        log.debug("Routing {} to {} with key: {}",
                method.getName(),
                strategy.getClass().getSimpleName(),
                key);

        // Invoke the method on the actual implementation
        return method.invoke(strategy, args);
    }

    private CompositeKey getRoutingKey(Method method, Object[] args) {
        if (contextKeys != null && contextKeys.length > 0) {
            // Use predefined context keys
            return StrategyContext.getCompositeKey(contextKeys);
        }

        // Try to extract from method parameters
        // Look for parameters annotated with @RoutingKey or named routing fields
        // For now, default to context
        return StrategyContext.getCompositeKey("platform");
    }
}
