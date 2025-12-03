package com.github.oxyethylene.springboot4demo.common.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

/**
 * Processor that scans for @StrategyClient interfaces and creates proxy implementations.
 * Similar to how Feign creates HTTP clients from interfaces.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyClientProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final Set<String> processedInterfaces = new HashSet<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // Note: Actual interface scanning would require classpath scanning
        // For now, this will process interfaces when they're referenced
        log.info("StrategyClientProcessor initialized - ready to create strategy client proxies");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Post-process after bean definitions are loaded
    }

    /**
     * Create a proxy instance for a @StrategyClient interface
     */
    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> interfaceClass, StrategyMappingRegistry registry) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("@StrategyClient can only be applied to interfaces: " + interfaceClass.getName());
        }

        StrategyClient annotation = AnnotationUtils.findAnnotation(interfaceClass, StrategyClient.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Interface must be annotated with @StrategyClient: " + interfaceClass.getName());
        }

        log.info("Creating strategy client proxy for interface: {}", interfaceClass.getName());

        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[]{interfaceClass},
            (proxy, method, args) -> {
                // Handle Object methods
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(proxy, args);
                }

                // Look for @StrategyMapping on the method
                StrategyMapping mapping = AnnotationUtils.findAnnotation(method, StrategyMapping.class);
                if (mapping != null && mapping.value().length > 0) {
                    String key = mapping.value()[0];
                    log.debug("Invoking strategy method {} with key: {}", method.getName(), key);
                    return registry.invoke(key, args);
                }

                // Check if there's a fallback handler
                if (annotation.fallbackHandler() != void.class) {
                    Object fallback = applicationContext.getBean(annotation.fallbackHandler());
                    return method.invoke(fallback, args);
                }

                throw new UnsupportedOperationException(
                    "Method " + method.getName() + " must be annotated with @StrategyMapping or a fallbackHandler must be specified");
            }
        );
    }
}
