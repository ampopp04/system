package com.system.inversion.util;

import com.system.util.clazz.ClassUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.system.util.collection.CollectionUtils.*;

/**
 * The <class>InversionUtils</class> defines
 * inversion related utility methods
 *
 * @author Andrew
 */
public class InversionUtils {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The base package used by our application
     */
    public static final String SYSTEM_PACKAGE_ROOT = "com";

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Util Methods                                                      //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Scan the classpath returning all classes who are assignable to the given
     * assignableToClassType
     *
     * @param assignableToClassType
     * @return
     */
    public static List<Class<?>> getAssignableClassList(Class assignableToClassType) {
        BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr, false);
        s.addIncludeFilter(new AssignableTypeFilter(assignableToClassType));
        s.setIncludeAnnotationConfig(false);
        s.scan(SYSTEM_PACKAGE_ROOT);

        List<Class<?>> entityClassList = new ArrayList<>();
        iterate(iterable(bdr.getBeanDefinitionNames()),
                (entityDefinitionName) ->
                        add(entityClassList,
                                ClassUtils.forNameOrNull(bdr.getBeanDefinition(entityDefinitionName).getBeanClassName())));

        return entityClassList;
    }

    /**
     * Register a bean definition into the given bean factory
     *
     * @param registry
     * @param beanName
     * @param classType
     * @param properties
     */
    public static BeanDefinition registerBeanDefinition(BeanDefinitionRegistry registry, String beanName, Class classType, MutablePropertyValues properties) {
        BeanDefinition beanDefinition = createBeanDefinition(classType, properties);
        registry.registerBeanDefinition(beanName, beanDefinition);
        return beanDefinition;
    }

    /**
     * Create a bean definition without properties
     *
     * @param classType
     * @return
     */
    public static BeanDefinition createBeanDefinition(Class classType) {
        return createBeanDefinition(classType, null);
    }

    /**
     * Create a bean definition for the given class type with the given properties
     *
     * @param classType
     * @param properties
     * @return
     */
    public static BeanDefinition createBeanDefinition(Class classType, MutablePropertyValues properties) {
        StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(classType);
        AnnotatedGenericBeanDefinition definition = new AnnotatedGenericBeanDefinition(annotationMetadata);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
        definition.setLazyInit(true);
        definition.setPropertyValues(properties);
        return definition;
    }

    /**
     * Retrieve all environment resource properties
     *
     * @param env
     * @return
     */
    public static Map<String, Object> getResourceProperties(Environment env) {
        List<ResourcePropertySource> resourcePropertySourceList = new ArrayList<>();
        iterate(((StandardEnvironment) env).getPropertySources().iterator(), (propertySource) -> {
            if (propertySource instanceof ResourcePropertySource) {
                resourcePropertySourceList.add((ResourcePropertySource) propertySource);
            }
        });

        Map<String, Object> propertyMap = new HashMap<>();
        iterate(resourcePropertySourceList, (propertySource) -> {
            propertyMap.putAll(propertySource.getSource());
        });

        return propertyMap;
    }
}
