package com.github.oxyethylene.springboot4demo.common.strategy;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Post-processor that scans for @StrategyHandler beans and registers
 * methods annotated with @StrategyMapping for automatic routing.
 *
 * This enables annotation-based strategy pattern similar to Spring MVC's @RequestMapping.
 */
@Component
public class StrategyMappingRegistry implements BeanPostProcessor {

    private final Map<String, StrategyMethodInvoker> methodRegistry = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();

        // Check if class is annotated with @StrategyHandler
        if (AnnotationUtils.findAnnotation(clazz, StrategyHandler.class) != null) {
            // Scan all methods for @StrategyMapping
            for (Method method : clazz.getDeclaredMethods()) {
                StrategyMapping mapping = AnnotationUtils.findAnnotation(method, StrategyMapping.class);
                if (mapping != null) {
                    // Register each key to this method
                    for (String key : mapping.value()) {
                        methodRegistry.put(key, new StrategyMethodInvoker(bean, method));
                    }
                }
            }
        }

        return bean;
    }

    /**
     * Invoke the strategy method for the given key
     */
    public Object invoke(String key, Object... args) {
        StrategyMethodInvoker invoker = methodRegistry.get(key);
        if (invoker == null) {
            throw new IllegalArgumentException("No strategy mapping found for key: " + key);
        }
        return invoker.invoke(args);
    }

    /**
     * Check if a strategy exists for the given key
     */
    public boolean hasMapping(String key) {
        return methodRegistry.containsKey(key);
    }

    /**
     * Helper class to encapsulate method invocation
     */
    private static class StrategyMethodInvoker {
        private final Object bean;
        private final Method method;

        public StrategyMethodInvoker(Object bean, Method method) {
            this.bean = bean;
            this.method = method;
            this.method.setAccessible(true);
        }

        public Object invoke(Object... args) {
            try {
                return method.invoke(bean, args);
            } catch (Exception e) {
                throw new RuntimeException("Failed to invoke strategy method: " + method.getName(), e);
            }
        }
    }
}
