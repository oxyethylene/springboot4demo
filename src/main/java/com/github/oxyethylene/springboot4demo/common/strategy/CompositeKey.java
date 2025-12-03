package com.github.oxyethylene.springboot4demo.common.strategy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Composite key for multi-field routing.
 * Use this when you need to route based on multiple criteria.
 *
 * Example:
 * <pre>
 * // Route by platform AND affiliate type
 * CompositeKey key = CompositeKey.of(platformType, affiliateType);
 *
 * @StrategyMapping("PROVIDER:ORGANIZATION")
 * public Affiliate handleProviderOrg(Request req) { ... }
 * </pre>
 */
@Getter
@EqualsAndHashCode
public class CompositeKey {

    private final String[] parts;
    private final String compositeValue;

    private CompositeKey(Object... parts) {
        this.parts = Arrays.stream(parts)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toArray(String[]::new);
        this.compositeValue = String.join(":", this.parts);
    }

    /**
     * Create a composite key from multiple parts
     *
     * @param parts the key parts (will be joined with ":")
     * @return composite key
     */
    public static CompositeKey of(Object... parts) {
        if (parts == null || parts.length == 0) {
            throw new IllegalArgumentException("Composite key must have at least one part");
        }
        return new CompositeKey(parts);
    }

    /**
     * Parse a composite key from a string
     *
     * @param compositeString the composite key string (e.g., "PROVIDER:ORGANIZATION")
     * @return composite key
     */
    public static CompositeKey parse(String compositeString) {
        if (compositeString == null || compositeString.isEmpty()) {
            throw new IllegalArgumentException("Composite key string cannot be null or empty");
        }
        String[] parts = compositeString.split(":");
        return new CompositeKey((Object[]) parts);
    }

    /**
     * Get the number of parts in this composite key
     */
    public int size() {
        return parts.length;
    }

    /**
     * Get a specific part by index
     */
    public String getPart(int index) {
        if (index < 0 || index >= parts.length) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + parts.length);
        }
        return parts[index];
    }

    /**
     * Convert to string representation (uses ":" as delimiter)
     */
    @Override
    public String toString() {
        return compositeValue;
    }
}
