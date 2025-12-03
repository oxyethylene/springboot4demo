package com.github.oxyethylene.springboot4demo.controller;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyDispatcher;
import com.github.oxyethylene.springboot4demo.entity.Affiliate;
import com.github.oxyethylene.springboot4demo.entity.request.CreateAffiliateRequest;
import com.github.oxyethylene.springboot4demo.enums.PlatformType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/affiliate")
@RequiredArgsConstructor
public class AffiliateController {

    private final StrategyDispatcher dispatcher;

    /**
     * Create affiliate using annotation-based strategy routing.
     * No need to manually get strategy - dispatcher handles it automatically!
     */
    @PostMapping("/create")
    public ResponseEntity<Affiliate> createAffiliate(@RequestBody CreateAffiliateRequest request) {
        PlatformType platformType = PlatformType.fromId(request.getPlatformId());

        // Dispatch automatically routes to the correct @StrategyMapping method
        Affiliate affiliate = dispatcher.dispatch(platformType, Affiliate.class, request);

        return ResponseEntity.ok(affiliate);
    }
}
