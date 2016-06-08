package com.system.inversion.util;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * The <class>InversionUtils</class> defines
 * inversion related utility methods
 *
 * @author Andrew
 */
public class InversionUtils {

    /**
     * Register a bean definition into the given bean factory
     *
     * @param beanFactory
     * @param beanName
     * @param classType
     * @param properties
     */
    public static void registerBeanDefinition(DefaultListableBeanFactory beanFactory, String beanName, Class classType, MutablePropertyValues properties) {
        RootBeanDefinition definition = new RootBeanDefinition();
        definition.setBeanClassName(classType.getName());
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
        definition.setLazyInit(true);
        definition.setPropertyValues(properties);
        beanFactory.registerBeanDefinition(beanName, definition);
    }
}
