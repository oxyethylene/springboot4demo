package com.github.oxyethylene.springboot4demo.service.impl;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationService;
import lombok.extern.slf4j.Slf4j;

/**
 * Subscriber platform implementation
 * Just mark with @StrategyMapping annotation with the routing key
 */
@Slf4j
@StrategyMapping(key = "SUBSCRIBER")
public class SubscriberAffiliateService implements AffiliateCreationService {

    @Override
    public Affiliate create(CreateAffiliateRequest request) {
        log.info("Creating SUBSCRIBER affiliate: {}", request.getName());

        // Subscriber-specific logic
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        // Additional subscriber-specific processing
        log.info("Subscriber affiliate created with special subscriber benefits");

        return affiliate;
    }

    @Override
    public void validate(CreateAffiliateRequest request) {
        log.debug("Validating subscriber affiliate request");
        // Subscriber-specific validation
    }
}
