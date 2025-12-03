package com.github.oxyethylene.springboot4demo.enums;

public enum PlatformType {
    SUBSCRIBER,
    PROVIDER,
    WEALTH;

    public static PlatformType fromId(Long platformId) {
        if (platformId == null) {
            throw new IllegalArgumentException("Platform ID cannot be null");
        }

        return switch (platformId.intValue()) {
            case 1 -> SUBSCRIBER;
            case 2 -> PROVIDER;
            case 3 -> WEALTH;
            default -> throw new IllegalArgumentException("Unknown platform ID: " + platformId);
        };
    }
}
