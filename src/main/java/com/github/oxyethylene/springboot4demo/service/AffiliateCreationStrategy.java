package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.Strategy;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.enums.PlatformType;

public interface AffiliateCreationStrategy extends Strategy<PlatformType> {

    /**
     * Get the platform type this strategy handles
     */
    @Override
    default PlatformType getStrategyKey() {
        return getPlatformType();
    }

    /**
     * Get the platform type this strategy handles
     */
    PlatformType getPlatformType();

    /**
     * Create an affiliate with platform-specific behavior
     */
    Affiliate createAffiliate(CreateAffiliateRequest request);
}
