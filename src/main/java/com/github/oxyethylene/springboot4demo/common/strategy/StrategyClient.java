package com.github.oxyethylene.springboot4demo.common.strategy;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Interface-based strategy client (similar to @FeignClient and @HttpExchange).
 * Define an interface with methods, and Spring will automatically create the implementation!
 *
 * NO NEED TO CREATE HANDLER CLASSES!
 *
 * Example:
 * <pre>
 * @StrategyClient
 * public interface AffiliateCreationService {
 *
 *     @StrategyMapping("SUBSCRIBER")
 *     Affiliate createSubscriberAffiliate(CreateAffiliateRequest request);
 *
 *     @StrategyMapping("PROVIDER")
 *     Affiliate createProviderAffiliate(CreateAffiliateRequest request);
 *
 *     @StrategyMapping("WEALTH")
 *     Affiliate createWealthAffiliate(CreateAffiliateRequest request);
 * }
 *
 * // Then just inject and use:
 * @RestController
 * @RequiredArgsConstructor
 * public class AffiliateController {
 *     private final AffiliateCreationService affiliateService;
 *
 *     @PostMapping("/affiliate")
 *     public Affiliate create(@RequestBody CreateAffiliateRequest req) {
 *         PlatformType type = PlatformType.fromId(req.getPlatformId());
 *
 *         // Spring routes to the correct method automatically!
 *         return switch(type) {
 *             case SUBSCRIBER -> affiliateService.createSubscriberAffiliate(req);
 *             case PROVIDER -> affiliateService.createProviderAffiliate(req);
 *             case WEALTH -> affiliateService.createWealthAffiliate(req);
 *         };
 *     }
 * }
 * </pre>
 *
 * Or even better, use with StrategyDispatcher for fully automatic routing:
 * <pre>
 * @RestController
 * @RequiredArgsConstructor
 * public class AffiliateController {
 *     private final StrategyDispatcher dispatcher;
 *
 *     @PostMapping("/affiliate")
 *     public Affiliate create(@RequestBody CreateAffiliateRequest req) {
 *         PlatformType type = PlatformType.fromId(req.getPlatformId());
 *         return dispatcher.dispatch(type, Affiliate.class, req);
 *     }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface StrategyClient {
    /**
     * The value may indicate a suggestion for a logical component name
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * Optional: Specify a handler class that implements this interface.
     * If not specified, methods must be annotated with @StrategyMapping
     * and a default implementation will be provided that throws UnsupportedOperationException.
     */
    Class<?> fallbackHandler() default void.class;
}
