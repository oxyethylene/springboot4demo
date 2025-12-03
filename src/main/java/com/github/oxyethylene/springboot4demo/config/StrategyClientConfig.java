package com.github.oxyethylene.springboot4demo.config;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyClientFactoryBean;
import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMappingRegistry;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for @StrategyClient interfaces.
 *
 * Register your @StrategyClient interfaces here to create automatic proxies.
 * This is similar to how you would configure Feign clients.
 *
 * Example:
 * <pre>
 * @Bean
 * public MyStrategyService myStrategyService(StrategyMappingRegistry registry) {
 *     return new StrategyClientFactoryBean<>(MyStrategyService.class, registry).getObject();
 * }
 * </pre>
 */
@Configuration
@RequiredArgsConstructor
public class StrategyClientConfig {

    private final StrategyMappingRegistry registry;

    /**
     * Create a proxy for AffiliateCreationService.
     * No implementation needed - Spring creates it automatically!
     */
    @Bean
    public AffiliateCreationService affiliateCreationService() throws Exception {
        return new StrategyClientFactoryBean<>(AffiliateCreationService.class, registry)
                .getObject();
    }

    // Add more @StrategyClient beans here as needed:

    /*
    @Bean
    public PaymentProcessingService paymentProcessingService() throws Exception {
        return new StrategyClientFactoryBean<>(PaymentProcessingService.class, registry)
                .getObject();
    }

    @Bean
    public NotificationService notificationService() throws Exception {
        return new StrategyClientFactoryBean<>(NotificationService.class, registry)
                .getObject();
    }
    */
}
