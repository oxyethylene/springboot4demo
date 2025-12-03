package com.github.oxyethylene.springboot4demo.controller;

import com.github.oxyethylene.springboot4demo.common.strategy.CompositeKey;
import com.github.oxyethylene.springboot4demo.common.strategy.StrategyDispatcher;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.enums.PlatformType;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Examples demonstrating different ways to use the strategy pattern framework.
 * Choose the approach that best fits your needs!
 */
@RestController
@RequestMapping("/examples/affiliate")
@RequiredArgsConstructor
public class AffiliateExampleController {

    private final StrategyDispatcher dispatcher;
    private final AffiliateCreationService affiliateService; // Interface-based client

    /**
     * Approach 1: Simple single-key routing with StrategyDispatcher
     * Most common use case - route by one field (platform)
     */
    @PostMapping("/simple")
    public ResponseEntity<Affiliate> createSimple(@RequestBody CreateAffiliateRequest request) {
        PlatformType platformType = PlatformType.fromId(request.getPlatformId());

        // One line - automatically routes to correct @StrategyMapping
        Affiliate affiliate = dispatcher.dispatch(platformType, Affiliate.class, request);

        return ResponseEntity.ok(affiliate);
    }

    /**
     * Approach 2: Composite key routing (multiple fields)
     * Use when routing depends on multiple criteria
     *
     * Example: Route by BOTH platform AND affiliate type
     */
    @PostMapping("/composite")
    public ResponseEntity<Affiliate> createWithCompositeKey(
            @RequestBody CreateAffiliateRequest request,
            @RequestParam String affiliateType) {

        PlatformType platformType = PlatformType.fromId(request.getPlatformId());

        // Create composite key: "PROVIDER:ORGANIZATION", "WEALTH:INDIVIDUAL", etc.
        CompositeKey key = CompositeKey.of(platformType, affiliateType);

        // Dispatch with composite key
        Affiliate affiliate = dispatcher.dispatch(key, Affiliate.class, request);

        return ResponseEntity.ok(affiliate);
    }

    /**
     * Approach 3: Interface-based client (like Feign)
     * Define interface with @StrategyClient, no implementation needed!
     *
     * Good when you want explicit method names and type safety
     */
    @PostMapping("/interface/subscriber")
    public ResponseEntity<Affiliate> createSubscriber(@RequestBody CreateAffiliateRequest request) {
        // Call interface method directly - Spring routes automatically
        Affiliate affiliate = affiliateService.createSubscriberAffiliate(request);
        return ResponseEntity.ok(affiliate);
    }

    @PostMapping("/interface/provider")
    public ResponseEntity<Affiliate> createProvider(@RequestBody CreateAffiliateRequest request) {
        Affiliate affiliate = affiliateService.createProviderAffiliate(request);
        return ResponseEntity.ok(affiliate);
    }

    @PostMapping("/interface/wealth")
    public ResponseEntity<Affiliate> createWealth(@RequestBody CreateAffiliateRequest request) {
        Affiliate affiliate = affiliateService.createWealthAffiliate(request);
        return ResponseEntity.ok(affiliate);
    }

    /**
     * Approach 4: Interface-based with composite keys
     * Combine interface approach with composite key routing
     */
    @PostMapping("/interface/composite/provider-org")
    public ResponseEntity<Affiliate> createProviderOrg(@RequestBody CreateAffiliateRequest request) {
        Affiliate affiliate = affiliateService.createProviderOrganization(request);
        return ResponseEntity.ok(affiliate);
    }

    @PostMapping("/interface/composite/wealth-org")
    public ResponseEntity<Affiliate> createWealthOrg(@RequestBody CreateAffiliateRequest request) {
        Affiliate affiliate = affiliateService.createWealthOrganization(request);
        return ResponseEntity.ok(affiliate);
    }

    /**
     * Approach 5: Dynamic routing with validation
     * Check if handler exists before dispatching
     */
    @PostMapping("/dynamic")
    public ResponseEntity<Affiliate> createDynamic(
            @RequestBody CreateAffiliateRequest request,
            @RequestParam(required = false) String affiliateType) {

        PlatformType platformType = PlatformType.fromId(request.getPlatformId());

        Object key;
        if (affiliateType != null) {
            // Use composite key if type is provided
            key = CompositeKey.of(platformType, affiliateType);
        } else {
            // Use simple key
            key = platformType;
        }

        // Validate handler exists
        if (!dispatcher.hasHandler(key)) {
            return ResponseEntity.badRequest().build();
        }

        Affiliate affiliate = dispatcher.dispatch(key, Affiliate.class, request);
        return ResponseEntity.ok(affiliate);
    }

    /**
     * Approach 6: Pure string-based routing
     * For maximum flexibility with external configuration
     */
    @PostMapping("/string/{strategyKey}")
    public ResponseEntity<Affiliate> createByString(
            @PathVariable String strategyKey,
            @RequestBody CreateAffiliateRequest request) {

        // Strategy key can come from path, config, database, etc.
        // Examples: "PROVIDER", "WEALTH:ORGANIZATION", "CUSTOM_STRATEGY"

        if (!dispatcher.hasHandler(strategyKey)) {
            return ResponseEntity.notFound().build();
        }

        Affiliate affiliate = dispatcher.dispatch(strategyKey, Affiliate.class, request);
        return ResponseEntity.ok(affiliate);
    }
}
