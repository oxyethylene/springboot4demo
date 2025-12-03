package com.github.oxyethylene.springboot4demo.service.impl;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationService;
import lombok.extern.slf4j.Slf4j;

/**
 * Provider platform implementation
 */
@Slf4j
@StrategyMapping(key = "PROVIDER")
public class ProviderAffiliateService implements AffiliateCreationService {

    @Override
    public Affiliate create(CreateAffiliateRequest request) {
        log.info("Creating PROVIDER affiliate: {}", request.getName());

        // Provider-specific logic
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        // Additional provider-specific processing
        log.info("Provider affiliate created with provider permissions");

        return affiliate;
    }

    @Override
    public void validate(CreateAffiliateRequest request) {
        log.debug("Validating provider affiliate request");
        // Provider-specific validation
    }
}
