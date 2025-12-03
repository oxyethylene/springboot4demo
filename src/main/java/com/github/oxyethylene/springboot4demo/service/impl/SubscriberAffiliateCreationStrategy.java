package com.github.oxyethylene.springboot4demo.service.impl;

import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.enums.PlatformType;
import com.github.oxyethylene.springboot4demo.service.AffiliateCreationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubscriberAffiliateCreationStrategy implements AffiliateCreationStrategy {

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.SUBSCRIBER;
    }

    @Override
    public Affiliate createAffiliate(CreateAffiliateRequest request) {
        log.info("Creating affiliate for SUBSCRIBER platform with name: {}", request.getName());

        // Subscriber-specific logic here
        // For example: additional validation, specific notifications, etc.

        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());

        // TODO: Save to database
        log.info("Subscriber affiliate created successfully");

        return affiliate;
    }
}
