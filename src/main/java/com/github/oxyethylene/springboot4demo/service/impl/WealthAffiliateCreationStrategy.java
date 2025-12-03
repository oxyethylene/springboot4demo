package com.github.oxyethylene.springboot4demo.service.impl;

import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.enums.PlatformType;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WealthAffiliateCreationStrategy implements AffiliateCreationStrategy {

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.WEALTH;
    }

    @Override
    public Affiliate createAffiliate(CreateAffiliateRequest request) {
        log.info("Creating affiliate for WEALTH platform with name: {}", request.getName());

        // Wealth-specific logic here
        // For example: financial compliance checks, wealth management setup, etc.

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        // TODO: Save to database
        log.info("Wealth affiliate created successfully");

        return affiliate;
    }
}
