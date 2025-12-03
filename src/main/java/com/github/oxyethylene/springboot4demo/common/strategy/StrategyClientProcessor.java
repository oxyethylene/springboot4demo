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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.util.HashSet;
import java.util.Set;

/**
 * Processor that scans for @StrategyClient interfaces and registers them as beans
 * Creates dynamic proxies automatically
 */
@Slf4j
@RequiredArgsConstructor
public class StrategyClientProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private final StrategyMappingRegistry registry;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            // Scan for @StrategyClient annotated interfaces
            Set<Class<?>> clientInterfaces = scanStrategyClients();

            // Register each interface as a bean with dynamic proxy
            for (Class<?> interfaceClass : clientInterfaces) {
                registerStrategyClient(registry, interfaceClass);
            }
        } catch (Exception e) {
            log.error("Error scanning for strategy clients", e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // No-op
    }

    private Set<Class<?>> scanStrategyClients() throws Exception {
        Set<Class<?>> clients = new HashSet<>();

        // Get base package from application context
        String basePackage = getBasePackage();
        String packageSearchPath = "classpath*:" + basePackage.replace('.', '/') + "/**/*.class";

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

        Resource[] resources = resolver.getResources(packageSearchPath);

        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                String className = metadataReader.getClassMetadata().getClassName();

                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isInterface() && clazz.isAnnotationPresent(StrategyClient.class)) {
                        clients.add(clazz);
                        log.info("Found @StrategyClient interface: {}", className);
                    }
                } catch (ClassNotFoundException e) {
                    log.warn("Could not load class: {}", className);
                }
            }
        }

        return clients;
    }

    private String getBasePackage() {
        // Try to detect base package from main application class
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            try {
                Class<?> beanClass = applicationContext.getType(beanName);
                if (beanClass != null && beanClass.getName().endsWith("Application")) {
                    return beanClass.getPackage().getName();
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        // Default fallback
        return "com.github.oxyethylene.springboot4demo";
    }

    private void registerStrategyClient(BeanDefinitionRegistry registry, Class<?> interfaceClass) {
        String beanName = getBeanName(interfaceClass);

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(StrategyClientFactoryBean.class);
        beanDefinition.setPrimary(true);  // Mark as primary to avoid conflicts
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(interfaceClass);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(this.registry);

        registry.registerBeanDefinition(beanName, beanDefinition);

        log.info("Registered strategy client bean: {} for interface: {}",
                beanName, interfaceClass.getSimpleName());
    }

    private String getBeanName(Class<?> interfaceClass) {
        String simpleName = interfaceClass.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
