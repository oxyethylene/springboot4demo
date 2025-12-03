package com.github.oxyethylene.springboot4demo.common.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that scans and stores all @StrategyMapping annotated beans
 * Automatically registers strategies on application startup
 */
@Slf4j
public class StrategyMappingRegistry implements BeanPostProcessor {

    // Map of interface -> (composite key -> implementation bean)
    private final Map<Class<?>, Map<CompositeKey, Object>> strategyMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        // Check if bean has @StrategyMapping annotation
        StrategyMapping mapping = beanClass.getAnnotation(StrategyMapping.class);
        if (mapping != null) {
            registerStrategy(bean, mapping);
        }

        return bean;
    }

    private void registerStrategy(Object bean, StrategyMapping mapping) {
        // Get all interfaces implemented by this bean
        Class<?>[] interfaces = bean.getClass().getInterfaces();

        CompositeKey key = CompositeKey.of(mapping.key());

        for (Class<?> interfaceClass : interfaces) {
            // Skip Spring internal interfaces
            if (interfaceClass.getName().startsWith("org.springframework")) {
                continue;
            }

            strategyMap
                .computeIfAbsent(interfaceClass, k -> new ConcurrentHashMap<>())
                .put(key, bean);

            log.info("Registered strategy: {} -> {} with key: {}",
                    interfaceClass.getSimpleName(),
                    bean.getClass().getSimpleName(),
                    key);
        }
    }

    /**
     * Get strategy implementation for given interface and key
     */
    @SuppressWarnings("unchecked")
    public <T> T getStrategy(Class<T> interfaceClass, CompositeKey key) {
        Map<CompositeKey, Object> strategies = strategyMap.get(interfaceClass);

        if (strategies == null) {
            throw new IllegalArgumentException(
                "No strategies registered for interface: " + interfaceClass.getName());
        }

        Object strategy = strategies.get(key);

        if (strategy == null) {
            throw new IllegalArgumentException(
                String.format("No strategy found for interface: %s with key: %s. Available keys: %s",
                    interfaceClass.getSimpleName(), key, strategies.keySet()));
        }

        return (T) strategy;
    }

    /**
     * Get strategy implementation for given interface and single key
     */
    public <T> T getStrategy(Class<T> interfaceClass, String key) {
        return getStrategy(interfaceClass, CompositeKey.of(key));
    }

    /**
     * Check if strategy exists
     */
    public boolean hasStrategy(Class<?> interfaceClass, CompositeKey key) {
        Map<CompositeKey, Object> strategies = strategyMap.get(interfaceClass);
        return strategies != null && strategies.containsKey(key);
    }
}
