package com.github.oxyethylene.springboot4demo.common.strategy;

/**
 * Generic strategy interface for implementing strategy pattern
 * @param <K> The key type used to identify different strategies
 */
public interface Strategy<K> {

    /**
     * Get the key that identifies this strategy
     * @return the strategy key
     */
    K getStrategyKey();
}
