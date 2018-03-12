/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.generator.type;

import com.system.bean.type.SystemBeanType;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportGeneratorType</class> defines a
 * type of generator that is allowed for export generators of this type.
 *
 * @author Andrew
 */
public class SystemExportGeneratorType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Defines the type of executor beans that are allowed for export generators of this type.
     */
    @ManyToOne
    @JoinColumn(name = "system_bean_type_id")
    private SystemBeanType systemBeanType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportGeneratorType() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanType getSystemBeanType() {
        return systemBeanType;
    }

    public void setSystemBeanType(SystemBeanType systemBeanType) {
        this.systemBeanType = systemBeanType;
    }
}