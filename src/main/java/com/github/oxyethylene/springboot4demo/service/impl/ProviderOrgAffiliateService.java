package com.github.oxyethylene.springboot4demo.service.impl;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationService;
import lombok.extern.slf4j.Slf4j;

/**
 * Provider + Organization implementation
 * Uses composite key routing for multi-dimensional strategy selection
 * This handles the specific case of PROVIDER platform with ORGANIZATION type
 */
@Slf4j
@StrategyMapping(key = {"PROVIDER", "ORGANIZATION"})
public class ProviderOrgAffiliateService implements AffiliateCreationService {

    @Override
    public Affiliate create(CreateAffiliateRequest request) {
        log.info("Creating PROVIDER ORGANIZATION affiliate: {}", request.getName());

        // Provider + Organization specific logic
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        // Special handling for provider organizations
        // e.g., bulk user management, corporate structure, etc.
        log.info("Provider organization affiliate created with corporate features");

        return affiliate;
    }

    @Override
    public void validate(CreateAffiliateRequest request) {
        log.debug("Validating provider organization affiliate request");
        // Provider organization specific validation
        // e.g., require corporate documents, tax ID, etc.
    }
}
