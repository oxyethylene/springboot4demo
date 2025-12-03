package com.github.oxyethylene.springboot4demo.service.impl;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationService;
import lombok.extern.slf4j.Slf4j;

/**
 * Wealth platform implementation
 */
@Slf4j
@StrategyMapping(key = "WEALTH")
public class WealthAffiliateService implements AffiliateCreationService {

    @Override
    public Affiliate create(CreateAffiliateRequest request) {
        log.info("Creating WEALTH affiliate: {}", request.getName());

        // Wealth-specific logic
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        // Additional wealth-specific processing
        log.info("Wealth affiliate created with financial compliance checks");

        return affiliate;
    }

    @Override
    public void validate(CreateAffiliateRequest request) {
        log.debug("Validating wealth affiliate request");
        // Wealth-specific validation
    }
}
