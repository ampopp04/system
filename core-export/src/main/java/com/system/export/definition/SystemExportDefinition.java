/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.definition;

import com.system.bean.type.SystemBeanType;
import com.system.db.entity.named.NamedEntity;
import com.system.export.SystemExport;
import com.system.export.definition.type.SystemExportDefinitionType;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportDefinition</class> defines an open ended
 * definition of a given {@link SystemExport}
 * <p>
 * This class defines various defaults that would be used for a given {@link SystemExport}
 * This might be details specific to generation such as start/end dates for when SystemExports of this definition
 * can be valid for.
 * <p>
 * It may also define a {@link SystemBeanType} which would be used to dynamically generate UI fields
 * for Exports of this given definition. A PDF or doc Export Report might have specific Export settings specific to those
 * exports vs an Export of a database Query.
 * <p>
 * This also may contain entity modify conditions under which Exports of this definition can be modified, created,deleted by various people
 *
 * @author Andrew
 */
public class SystemExportDefinition extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_export_definition_type_id")
    private SystemExportDefinitionType systemExportDefinitionType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportDefinition() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportDefinitionType getSystemExportDefinitionType() {
        return systemExportDefinitionType;
    }

    public void setSystemExportDefinitionType(SystemExportDefinitionType systemExportDefinitionType) {
        this.systemExportDefinitionType = systemExportDefinitionType;
    }
}