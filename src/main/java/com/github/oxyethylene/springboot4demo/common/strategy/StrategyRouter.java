package com.github.oxyethylene.springboot4demo.common.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Generic strategy router that automatically registers and routes to appropriate strategies
 * @param <K> The key type used to identify strategies
 * @param <S> The strategy type
 */
@Component
public class StrategyRouter<K, S extends Strategy<K>> {

    private final Map<K, S> strategyMap;

    public StrategyRouter(List<S> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        Strategy::getStrategyKey,
                        Function.identity()
                ));
    }

    /**
     * Get the strategy for the given key
     * @param key the strategy key
     * @return the strategy instance
     * @throws IllegalArgumentException if no strategy found for the key
     */
    public S route(K key) {
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
        return strategyMap.containsKey(key);
    }

    /**
     * Get all registered strategy keys
     * @return set of all strategy keys
     */
    public java.util.Set<K> getAvailableKeys() {
        return strategyMap.keySet();
    }
}
