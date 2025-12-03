package com.github.oxyethylene.springboot4demo.entity.request;

import lombok.Data;

@Data
public class CreateAffiliateRequest {
    private Long platformId;

    private String name;

    private String currency;

    private String timezone;

    private String countryCode;
}
