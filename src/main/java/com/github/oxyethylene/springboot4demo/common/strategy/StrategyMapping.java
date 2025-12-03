package com.github.oxyethylene.springboot4demo.common.strategy;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark strategy implementations with their routing keys
 * Supports single or multiple keys for flexible routing
 *
 * Note: This creates Spring beans but they won't be directly injected.
 * They are registered in StrategyMappingRegistry for routing purposes.
 *
 * Example single key:
 * @StrategyMapping(key = "SUBSCRIBER")
 *
 * Example multiple keys (composite):
 * @StrategyMapping(key = {"PROVIDER", "PREMIUM"})
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface StrategyMapping {

    /**
     * The routing key(s) that identify this strategy
     * Can be single key or composite keys
     */
    String[] key();

    /**
     * Optional: The strategy type this mapping belongs to
     * Useful when you have multiple strategy types in the same application
     */
    Class<?> type() default void.class;
}
