package com.github.oxyethylene.springboot4demo.common.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory bean that creates dynamic proxies for @StrategyClient interfaces
 */
@RequiredArgsConstructor
public class StrategyClientFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> interfaceClass;
    private final StrategyMappingRegistry registry;

    @Override
    public T getObject() {
        StrategyClient annotation = interfaceClass.getAnnotation(StrategyClient.class);
        String routingKey = annotation != null ? annotation.routingKey() : "";

        String[] contextKeys = routingKey.isEmpty()
            ? new String[]{"platform"}
            : routingKey.split(",");

        return StrategyProxy.create(interfaceClass, registry, contextKeys);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
