package com.github.oxyethylene.springboot4demo.common.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Factory bean for creating @StrategyClient proxy instances.
 * This integrates seamlessly with Spring's bean creation lifecycle.
 */
@Slf4j
@RequiredArgsConstructor
public class StrategyClientFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> interfaceClass;
    private final StrategyMappingRegistry registry;

    @Override
    public T getObject() throws Exception {
        return createProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("unchecked")
    private T createProxy() {
        log.info("Creating strategy client proxy for: {}", interfaceClass.getName());

        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[]{interfaceClass},
            (proxy, method, args) -> {
                // Handle Object methods
                if (method.getDeclaringClass() == Object.class) {
                    if ("toString".equals(method.getName())) {
                        return "StrategyClient proxy for " + interfaceClass.getSimpleName();
                    }
                    if ("hashCode".equals(method.getName())) {
                        return System.identityHashCode(proxy);
                    }
                    if ("equals".equals(method.getName())) {
                        return proxy == args[0];
                    }
                    return method.invoke(this, args);
                }

                // Look for @StrategyMapping on the method
                StrategyMapping mapping = AnnotationUtils.findAnnotation(method, StrategyMapping.class);
                if (mapping != null && mapping.value().length > 0) {
                    String key = mapping.value()[0];
                    log.debug("Routing to strategy: {} for method: {}", key, method.getName());
                    return registry.invoke(key, args);
                }

                // No mapping found
                throw new UnsupportedOperationException(
                    "Method " + method.getName() + " in " + interfaceClass.getSimpleName() +
                    " must be annotated with @StrategyMapping");
            }
        );
    }
}
