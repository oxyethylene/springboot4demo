package com.github.oxyethylene.springboot4demo.config;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyClientProcessor;
import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMappingRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for strategy pattern framework
 * Enables automatic strategy routing
 */
@Configuration
public class StrategyConfig {

    @Bean
    @ConditionalOnMissingBean
    public StrategyMappingRegistry strategyMappingRegistry() {
        return new StrategyMappingRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public StrategyClientProcessor strategyClientProcessor(StrategyMappingRegistry registry) {
        return new StrategyClientProcessor(registry);
    }
}
