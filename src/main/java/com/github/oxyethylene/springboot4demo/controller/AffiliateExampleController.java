package com.github.oxyethylene.springboot4demo.controller;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyContext;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.enums.PlatformType;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Example controller showing the new annotation-based approach
 * Simply inject the service interface and it automatically routes!
 */
@RestController
@RequestMapping("/api/affiliate")
@RequiredArgsConstructor
public class AffiliateExampleController {

    // Just inject the interface - routing happens automatically!
    private final AffiliateCreationService affiliateService;

    /**
     * Example 1: Single key routing (by platform only)
     */
    @PostMapping("/create")
    public ResponseEntity<Affiliate> createAffiliate(@RequestBody CreateAffiliateRequest request) {
        // Set routing context
        PlatformType platformType = PlatformType.fromId(request.getPlatformId());
        StrategyContext.setPlatform(platformType.name());

        try {
            // Call the service - it automatically routes to correct implementation!
            Affiliate affiliate = affiliateService.create(request);
            return ResponseEntity.ok(affiliate);
        } finally {
            // Clean up context
            StrategyContext.clear();
        }
    }

    /**
     * Example 2: Composite key routing (by platform AND affiliate type)
     * For example: PROVIDER + ORGANIZATION
     */
    @PostMapping("/create-advanced")
    public ResponseEntity<Affiliate> createAffiliateAdvanced(
            @RequestBody CreateAffiliateRequest request,
            @RequestParam String affiliateType) {

        // Set multiple routing keys
        PlatformType platformType = PlatformType.fromId(request.getPlatformId());
        StrategyContext.setPlatform(platformType.name());
        StrategyContext.setAffiliateType(affiliateType);

        try {
            // Same service call, but routes to composite key implementation!
            Affiliate affiliate = affiliateService.create(request);
            return ResponseEntity.ok(affiliate);
        } finally {
            StrategyContext.clear();
        }
    }

    /**
     * Example 3: Using path variable for routing
     */
    @PostMapping("/{platform}/create")
    public ResponseEntity<Affiliate> createAffiliateByPath(
            @PathVariable String platform,
            @RequestBody CreateAffiliateRequest request) {

        StrategyContext.setPlatform(platform.toUpperCase());

        try {
            Affiliate affiliate = affiliateService.create(request);
            return ResponseEntity.ok(affiliate);
        } finally {
            StrategyContext.clear();
        }
    }
}
