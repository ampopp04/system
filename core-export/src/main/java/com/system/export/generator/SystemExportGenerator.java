/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.generator;

import com.system.bean.SystemBean;
import com.system.db.entity.named.NamedEntity;
import com.system.export.generator.type.SystemExportGeneratorType;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportGenerator</class> defines an instance
 * of a specific export generator. This generator is used to actually perform
 * the behind the scenes generation of the export content.
 *
 * @author Andrew
 */
public class SystemExportGenerator extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_export_generator_type_id")
    private SystemExportGeneratorType systemExportGeneratorType;

    /**
     * Implementation bean that executes the export content generation
     */
    @ManyToOne
    @JoinColumn(name = "system_bean_id")
    private SystemBean systemBean;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportGenerator() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportGeneratorType getSystemExportGeneratorType() {
        return systemExportGeneratorType;
    }

    public void setSystemExportGeneratorType(SystemExportGeneratorType systemExportGeneratorType) {
        this.systemExportGeneratorType = systemExportGeneratorType;
    }

    public SystemBean getSystemBean() {
        return systemBean;
    }

    public void setSystemBean(SystemBean systemBean) {
        this.systemBean = systemBean;
    }
}