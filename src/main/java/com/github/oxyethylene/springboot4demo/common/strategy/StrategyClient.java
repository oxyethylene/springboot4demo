package com.github.oxyethylene.springboot4demo.common.strategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark an interface as a strategy client
 * When injected, it will automatically route to the correct implementation
 * based on the routing context
 *
 * Usage:
 * @StrategyClient
 * public interface AffiliateService {
 *     Affiliate create(CreateRequest request);
 * }
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyClient {

    /**
     * The routing key parameter name in method arguments
     * Default is to use ThreadLocal context
     */
    String routingKey() default "";
}
