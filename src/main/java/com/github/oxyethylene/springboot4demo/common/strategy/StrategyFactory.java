package com.github.oxyethylene.springboot4demo.common.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.Map;

/**
 * Generic strategy factory that creates type-safe strategy routers
 * Usage: Extend this class and specify your key and strategy types
 *
 * @param <K> The key type used to identify strategies
 * @param <S> The strategy type
 */
public abstract class StrategyFactory<K, S extends Strategy<K>> {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<K, S> strategyMap;

    /**
     * Get the strategy for the given key
     * @param key the strategy key
     * @return the strategy instance
     */
    public S getStrategy(K key) {
        if (strategyMap == null) {
            initializeStrategies();
        }
        S strategy = strategyMap.get(key);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for key: " + key);
        }
        return strategy;
    }

    /**
     * Check if a strategy exists for the given key
     * @param key the strategy key
     * @return true if strategy exists, false otherwise
     */
    public boolean hasStrategy(K key) {
        if (strategyMap == null) {
            initializeStrategies();
        }
        return strategyMap.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    private void initializeStrategies() {
        ResolvableType type = ResolvableType.forClass(getClass()).as(StrategyFactory.class);
        Class<S> strategyClass = (Class<S>) type.getGeneric(1).resolve();

        Map<String, S> beans = applicationContext.getBeansOfType(strategyClass);
        strategyMap = beans.values().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Strategy::getStrategyKey,
                        java.util.function.Function.identity()
                ));
    }
}
