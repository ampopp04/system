/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.bean.util;

import com.system.bean.definition.SystemBeanDefinition;
import com.system.bean.type.SystemBeanType;
import com.system.bean.variable.definition.SystemBeanVariableDefinition;
import com.system.db.schema.table.SchemaTable;

import static com.system.util.clazz.ClassUtils.getFirstInterfaceOfClass;
import static com.system.util.string.StringUtils.addSpaceOnCapitialLetters;
import static com.system.util.string.StringUtils.lowercase;

/**
 * The <class>SystemBeanUtils</class> defines
 * system bean related utility methods
 *
 * @author Andrew
 */
public class SystemBeanUtils {

    /**
     * Create the System Bean Definition for a given BeanType class. This requires
     * that this class implements an interface that represents the System Bean Definition.
     *
     * @param beanType
     * @return
     */
    public static SystemBeanDefinition getStandardSystemBeanDefinitionFromBeanTypeClass(Class beanType) {
        return getSystemBeanDefinitionFromBeanDefinitionClass(getFirstInterfaceOfClass(beanType));
    }

    /**
     * Create the System Bean Type  for a given bean type class.
     *
     * @param beanType
     * @return
     */
    public static SystemBeanType getSystemBeanTypeFromBeanTypeClass(Class beanType) {
        return createSystemBeanType(
                addSpaceOnCapitialLetters(beanType.getSimpleName()),
                "The definition for " + lowercase(addSpaceOnCapitialLetters(beanType.getSimpleName())),
                beanType.getName()
        );
    }

    /**
     * Returns the name of a given System Bean Definition for a given Bean Type class.
     *
     * @param beanType
     * @return
     */
    public static String getBeanDefinitionNameFromBeanTypeClass(Class beanType) {
        return addSpaceOnCapitialLetters(getFirstInterfaceOfClass(beanType).getSimpleName());
    }

    /**
     * Returns the System Bean Definition for a provided bean definition class
     *
     * @param beanDefinition
     * @return
     */
    public static SystemBeanDefinition getSystemBeanDefinitionFromBeanDefinitionClass(Class beanDefinition) {
        return createSystemBeanDefinition(
                addSpaceOnCapitialLetters(beanDefinition.getSimpleName()),
                "The definition for " + lowercase(addSpaceOnCapitialLetters(beanDefinition.getSimpleName())),
                beanDefinition.getName()
        );
    }

    /**
     * Create a System Bean Definition
     *
     * @param name
     * @param description
     * @param className
     * @return
     */
    public static SystemBeanDefinition createSystemBeanDefinition(String name, String description, String className) {
        SystemBeanDefinition entity = new SystemBeanDefinition();
        entity.setName(name);
        entity.setDescription(description);
        entity.setClassName(className);
        return entity;
    }

    /**
     * Create a System Bean Type
     *
     * @param name
     * @param description
     * @param className
     * @return
     */
    public static SystemBeanType createSystemBeanType(String name, String description, String className) {
        SystemBeanType entity = new SystemBeanType();
        entity.setName(name);
        entity.setDescription(description);
        entity.setClassName(className);
        return entity;
    }

    /**
     * Create a System Bean Variable Definition
     *
     * @param name
     * @param description
     * @param variableName
     * @param schemaTable
     * @return
     */
    public static SystemBeanVariableDefinition createSystemBeanVariableDefinition(String name, String description, String variableName, SchemaTable schemaTable) {
        SystemBeanVariableDefinition entity = new SystemBeanVariableDefinition();
        entity.setName(name);
        entity.setVariableName(variableName);
        entity.setDescription(description);
        entity.setSchemaTable(schemaTable);
        return entity;
    }
}