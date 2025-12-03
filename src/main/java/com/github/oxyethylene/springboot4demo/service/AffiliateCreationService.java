package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyClient;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;

/**
 * Strategy client interface for affiliate creation
 * Just define methods like a normal service interface
 * The framework will automatically route to the correct implementation
 */
@StrategyClient
public interface AffiliateCreationService {

    /**
     * Create an affiliate
     * Will route to different implementations based on platform context
     */
    Affiliate create(CreateAffiliateRequest request);

    /**
     * Validate affiliate creation request
     */
    void validate(CreateAffiliateRequest request);
}
