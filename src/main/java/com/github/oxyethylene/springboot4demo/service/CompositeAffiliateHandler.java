package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyHandler;
import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for composite key routing examples.
 * Demonstrates routing based on multiple fields (Platform + Affiliate Type).
 *
 * Use composite keys when you need more granular routing:
 * - Platform: PROVIDER, SUBSCRIBER, WEALTH
 * - Type: ORGANIZATION, INDIVIDUAL
 *
 * This creates routing like: "PROVIDER:ORGANIZATION", "WEALTH:INDIVIDUAL", etc.
 */
@Slf4j
@StrategyHandler
public class CompositeAffiliateHandler {

    /**
     * Composite key format: "PLATFORM:TYPE"
     * Use CompositeKey.of(platform, type) to create keys programmatically
     */

    @StrategyMapping("PROVIDER:ORGANIZATION")
    public Affiliate handleProviderOrganization(CreateAffiliateRequest request) {
        log.info("Creating PROVIDER ORGANIZATION affiliate: {}", request.getName());

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Provider + Organization specific logic:
        // - Corporate verification
        // - Tax ID validation
        // - Multi-user setup

        return affiliate;
    }

    @StrategyMapping("PROVIDER:INDIVIDUAL")
    public Affiliate handleProviderIndividual(CreateAffiliateRequest request) {
        log.info("Creating PROVIDER INDIVIDUAL affiliate: {}", request.getName());

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Provider + Individual specific logic:
        // - Personal KYC
        // - Single user setup

        return affiliate;
    }

    @StrategyMapping("WEALTH:ORGANIZATION")
    public Affiliate handleWealthOrganization(CreateAffiliateRequest request) {
        log.info("Creating WEALTH ORGANIZATION affiliate: {}", request.getName());

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Wealth + Organization specific logic:
        // - Financial institution verification
        // - Compliance checks
        // - Portfolio management setup

        return affiliate;
    }

    @StrategyMapping("WEALTH:INDIVIDUAL")
    public Affiliate handleWealthIndividual(CreateAffiliateRequest request) {
        log.info("Creating WEALTH INDIVIDUAL affiliate: {}", request.getName());

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Wealth + Individual specific logic:
        // - High net worth verification
        // - Personal wealth management

        return affiliate;
    }

    @StrategyMapping("SUBSCRIBER:INDIVIDUAL")
    public Affiliate handleSubscriberIndividual(CreateAffiliateRequest request) {
        log.info("Creating SUBSCRIBER INDIVIDUAL affiliate: {}", request.getName());

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Subscriber + Individual specific logic

        return affiliate;
    }

    /**
     * You can also handle multiple composite keys with one method
     */
    @StrategyMapping({"SUBSCRIBER:ORGANIZATION", "SUBSCRIBER:BUSINESS"})
    public Affiliate handleSubscriberOrganization(CreateAffiliateRequest request) {
        log.info("Creating SUBSCRIBER ORGANIZATION/BUSINESS affiliate: {}", request.getName());

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Subscriber + Organization/Business logic

        return affiliate;
    }
}
