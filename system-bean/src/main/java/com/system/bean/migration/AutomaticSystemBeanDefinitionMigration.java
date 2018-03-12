/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.bean.migration;

import com.system.bean.definition.SystemBeanDefinition;
import com.system.bean.type.SystemBeanType;
import com.system.bean.util.SystemBeanUtils;
import com.system.bean.variable.definition.SystemBeanVariableDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.List;

import static com.system.bean.util.SystemBeanUtils.*;
import static com.system.db.util.entity.EntityUtils.isEntityClass;
import static com.system.util.clazz.ClassUtils.getClassFields;
import static com.system.util.collection.CollectionUtils.*;
import static com.system.util.string.StringUtils.addSpaceOnCapitialLetters;
import static com.system.util.string.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.lowerCase;

/**
 * The <class>AutomaticSystemBeanDefinitionMigration</class> automatically
 * created the migration for a specific BeanType class.
 * <p>
 * Given a class that is to be made into a Bean Type this class will be inspected.
 * The immediate interface it implements will be used to either look up
 * a pre-existing Bean Definition or create a new one for it.
 * <p>
 * The class is then used to create the Bean Type and it's declared fields are reflectively iterated
 * over to create the Bean Variable Definitions.
 *
 * @author Andrew
 * @see SystemBeanDefinition
 * @see SystemBeanType
 * @see SystemBeanVariableDefinition
 */
public abstract class AutomaticSystemBeanDefinitionMigration extends SystemBeanDefinitionMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Properties                                                     //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * The current bean type to migrate
     */
    private Class beanTypeClass;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Main Logic                                                     //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The list of classes to migrate as Bean Types
     *
     * @return
     */
    protected abstract List<Class> getBeanTypeClasses();


    /**
     * Iterate over each Bean Type class and then call super to migrate it
     */
    @Override
    protected void insertData() {
        iterate(iterable(getBeanTypeClasses()), (beanTypeClass) -> {
            setBeanTypeClass(beanTypeClass);
            super.insertData();
        });
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Bean Definition                                                //////////
    //////////////////////////////////////////////////////////////////////

    protected SystemBeanDefinition retrieveSystemBeanDefinition() {
        SystemBeanDefinition beanDefinition = getSystemBeanDefinitionByName(getBeanDefinitionNameFromBeanTypeClass(getBeanTypeClass()));
        return beanDefinition == null ? SystemBeanUtils.getStandardSystemBeanDefinitionFromBeanTypeClass(getBeanTypeClass()) : beanDefinition;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Bean Type                                                     //////////
    //////////////////////////////////////////////////////////////////////

    protected SystemBeanType retrieveSystemBeanType() {
        return getSystemBeanTypeFromBeanTypeClass(getBeanTypeClass());
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Variable Definitions                                         //////////
    //////////////////////////////////////////////////////////////////////

    protected List<SystemBeanVariableDefinition> retrieveSystemBeanVariableDefinitionList() {
        return getSystemBeanVariableDefinitionListFromBeanTypeClass(getBeanTypeClass());
    }

    protected List<SystemBeanVariableDefinition> getSystemBeanVariableDefinitionListFromBeanTypeClass(Class beanType) {
        final List<SystemBeanVariableDefinition> variableDefinitionList = newList();

        iterate(iterable(getClassFields(beanType)), (field) -> {
            if (!isInversionManagedBean(field.getName())) {
                variableDefinitionList.add(getSystemBeanVariableDefinitionFromClassField(field));
            }
        });

        return variableDefinitionList;
    }

    protected SystemBeanVariableDefinition getSystemBeanVariableDefinitionFromClassField(Field field) {
        return createSystemBeanVariableDefinition(
                capitalize(addSpaceOnCapitialLetters(field.getName())),
                "The variable defining the " + lowerCase(addSpaceOnCapitialLetters(field.getName())),
                field.getName(),
                isEntityClass(field.getDeclaringClass()) ? schemaTableRepository.findByName(field.getDeclaringClass().getSimpleName()) : null);
    }

    protected boolean isInversionManagedBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Getter/Setters                                               //////////
    //////////////////////////////////////////////////////////////////////

    public Class getBeanTypeClass() {
        return beanTypeClass;
    }

    public void setBeanTypeClass(Class beanTypeClass) {
        this.beanTypeClass = beanTypeClass;
    }
}