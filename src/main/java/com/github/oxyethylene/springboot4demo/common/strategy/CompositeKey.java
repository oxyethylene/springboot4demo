package com.github.oxyethylene.springboot4demo.common.strategy;

import java.util.Arrays;
import java.util.Objects;

/**
 * Composite key for multi-dimensional routing
 * Supports routing based on multiple criteria
 */
public class CompositeKey {

    private final String[] keys;

    private CompositeKey(String... keys) {
        this.keys = keys;
    }

    public static CompositeKey of(String... keys) {
        return new CompositeKey(keys);
    }

    public static CompositeKey of(Object... keys) {
        String[] stringKeys = Arrays.stream(keys)
                .map(Object::toString)
                .toArray(String[]::new);
        return new CompositeKey(stringKeys);
    }

    public String[] getKeys() {
        return keys;
    }

    public String getKey(int index) {
        return index < keys.length ? keys[index] : null;
    }

    public int size() {
        return keys.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return Arrays.equals(keys, that.keys);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keys);
    }

    @Override
    public String toString() {
        return String.join(":", keys);
    }
}
