package com.github.oxyethylene.springboot4demo.enums;

public enum AffiliateType {
    INDIVIDUAL,
    ORGANIZATION,
    BUSINESS;

    public static AffiliateType fromString(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Affiliate type cannot be null");
        }

        try {
            return AffiliateType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown affiliate type: " + type);
        }
    }
}
