package com.github.oxyethylene.springboot4demo.controller;

import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.enums.PlatformType;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationStrategy;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/affiliate")
@RequiredArgsConstructor
public class AffiliateController {

    private final AffiliateCreationStrategyFactory strategyFactory;

    @PostMapping("/create")
    public ResponseEntity<Affiliate> createAffiliate(@RequestBody CreateAffiliateRequest request) {
        // Determine platform type from request
        PlatformType platformType = PlatformType.fromId(request.getPlatformId());

        // Get the appropriate strategy
        AffiliateCreationStrategy strategy = strategyFactory.getStrategy(platformType);

        // Execute platform-specific creation logic
        Affiliate affiliate = strategy.createAffiliate(request);

        return ResponseEntity.ok(affiliate);
    }
}
