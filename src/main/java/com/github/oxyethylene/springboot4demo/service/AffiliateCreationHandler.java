package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyHandler;
import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Annotation-based affiliate creation handler.
 * All platform-specific logic in one class with method-level routing.
 */
@Slf4j
@StrategyHandler
public class AffiliateCreationHandler {

    @StrategyMapping("SUBSCRIBER")
    public Affiliate handleSubscriber(CreateAffiliateRequest request) {
        log.info("Creating affiliate for SUBSCRIBER platform with name: {}", request.getName());

        // Subscriber-specific logic here
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        log.info("Subscriber affiliate created successfully");
        return affiliate;
    }

    @StrategyMapping("PROVIDER")
    public Affiliate handleProvider(CreateAffiliateRequest request) {
        log.info("Creating affiliate for PROVIDER platform with name: {}", request.getName());

        // Provider-specific logic here
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        log.info("Provider affiliate created successfully");
        return affiliate;
    }

    @StrategyMapping("WEALTH")
    public Affiliate handleWealth(CreateAffiliateRequest request) {
        log.info("Creating affiliate for WEALTH platform with name: {}", request.getName());

        // Wealth-specific logic here
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        log.info("Wealth affiliate created successfully");
        return affiliate;
    }
}
