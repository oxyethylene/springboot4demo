package com.github.oxyethylene.springboot4demo.common.strategy;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Annotation to mark a class as a strategy implementation with automatic routing.
 * Similar to @RestController, this annotation combines @Component with strategy metadata.
 *
 * Usage:
 * <pre>
 * @StrategyHandler
 * public class MyStrategy implements Strategy&lt;MyKeyType&gt; {
 *
 *     @StrategyMapping(MyKeyType.KEY_A)
 *     public MyResult handleA(MyRequest request) {
 *         // Implementation
 *     }
 *
 *     @StrategyMapping(MyKeyType.KEY_B)
 *     public MyResult handleB(MyRequest request) {
 *         // Implementation
 *     }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface StrategyHandler {
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     */
    String value() default "";
}
