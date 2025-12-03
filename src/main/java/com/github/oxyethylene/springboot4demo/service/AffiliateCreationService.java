package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyClient;
import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;

/**
 * Interface-based strategy client - NO IMPLEMENTATION NEEDED!
 *
 * Just define the interface with @StrategyClient and @StrategyMapping.
 * Spring will automatically create the implementation and route calls.
 *
 * This is similar to how @FeignClient and @HttpExchange work.
 */
@StrategyClient
public interface AffiliateCreationService {

    /**
     * Methods annotated with @StrategyMapping will be automatically routed
     * to the corresponding @StrategyHandler method
     */

    @StrategyMapping("SUBSCRIBER")
    Affiliate createSubscriberAffiliate(CreateAffiliateRequest request);

    @StrategyMapping("PROVIDER")
    Affiliate createProviderAffiliate(CreateAffiliateRequest request);

    @StrategyMapping("WEALTH")
    Affiliate createWealthAffiliate(CreateAffiliateRequest request);

    /**
     * Composite key example: Route by Platform + Affiliate Type
     * Use format "PLATFORM:TYPE" in @StrategyMapping
     */
    @StrategyMapping("PROVIDER:ORGANIZATION")
    Affiliate createProviderOrganization(CreateAffiliateRequest request);

    @StrategyMapping("PROVIDER:INDIVIDUAL")
    Affiliate createProviderIndividual(CreateAffiliateRequest request);

    @StrategyMapping("WEALTH:ORGANIZATION")
    Affiliate createWealthOrganization(CreateAffiliateRequest request);
}
