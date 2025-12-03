package com.github.oxyethylene.springboot4demo.common.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-local context for storing routing information
 * Allows automatic strategy routing without passing keys explicitly
 */
public class StrategyContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT =
        ThreadLocal.withInitial(HashMap::new);

    /**
     * Set a routing key value
     */
    public static void set(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    /**
     * Get a routing key value
     */
    public static Object get(String key) {
        return CONTEXT.get().get(key);
    }

    /**
     * Get a routing key value as String
     */
    public static String getString(String key) {
        Object value = get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * Set platform type for routing
     */
    public static void setPlatform(String platform) {
        set("platform", platform);
    }

    /**
     * Get platform type
     */
    public static String getPlatform() {
        return getString("platform");
    }

    /**
     * Set affiliate type for routing
     */
    public static void setAffiliateType(String affiliateType) {
        set("affiliateType", affiliateType);
    }

    /**
     * Get affiliate type
     */
    public static String getAffiliateType() {
        return getString("affiliateType");
    }

    /**
     * Create composite key from context
     */
    public static CompositeKey getCompositeKey(String... keys) {
        String[] values = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            values[i] = getString(keys[i]);
        }
        return CompositeKey.of(values);
    }

    /**
     * Clear the context for current thread
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
