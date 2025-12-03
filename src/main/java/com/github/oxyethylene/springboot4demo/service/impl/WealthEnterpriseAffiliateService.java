package com.github.oxyethylene.springboot4demo.service.impl;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationService;
import lombok.extern.slf4j.Slf4j;

/**
 * Wealth + Enterprise implementation
 * Composite key routing example
 */
@Slf4j
@StrategyMapping(key = {"WEALTH", "ENTERPRISE"})
public class WealthEnterpriseAffiliateService implements AffiliateCreationService {

    @Override
    public Affiliate create(CreateAffiliateRequest request) {
        log.info("Creating WEALTH ENTERPRISE affiliate: {}", request.getName());

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        // Wealth + Enterprise specific logic
        // e.g., dedicated account manager, custom compliance, SLA guarantees
        log.info("Wealth enterprise affiliate created with premium features");

        return affiliate;
    }

    @Override
    public void validate(CreateAffiliateRequest request) {
        log.debug("Validating wealth enterprise affiliate request");
        // Wealth enterprise specific validation
    }
}
