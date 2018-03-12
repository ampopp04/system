/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.bean.type;

import com.system.bean.definition.SystemBeanDefinition;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemBeanType</class> defines a specific instance of a
 * {@link SystemBeanDefinition}. This class would represent a
 * an actual instance of an Interface or class.
 *
 * @author Andrew
 */
public class SystemBeanType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_bean_definition_id")
    private SystemBeanDefinition systemBeanDefinition;

    /**
     * Represents the fully qualified package and class name
     */
    private String className;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanType() {
    }

    public static SystemBeanType newInstance(String name, String description, SystemBeanDefinition systemBeanDefinition, String className) {
        SystemBeanType entity = new SystemBeanType();
        entity.setName(name);
        entity.setDescription(description);
        entity.setSystemBeanDefinition(systemBeanDefinition);
        entity.setClassName(className);
        return entity;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanDefinition getSystemBeanDefinition() {
        return systemBeanDefinition;
    }

    public void setSystemBeanDefinition(SystemBeanDefinition systemBeanDefinition) {
        this.systemBeanDefinition = systemBeanDefinition;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}