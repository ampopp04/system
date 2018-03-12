/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.task.type;

import com.system.db.entity.named.NamedEntity;

/**
 * The <class>SystemExportTaskType</class> defines
 * a type of export task relating to details such as what department it is related to or
 * who can modify export information of this type.
 * <p>
 * Export Types Ex.
 * Accounting
 * Project Management
 * Sales
 * Engineering
 * Marketing
 *
 * @author Andrew
 */
public class SystemExportTaskType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTaskType() {
    }

    public static SystemExportTaskType newInstance(String name, String description) {
        SystemExportTaskType systemExportDefinitionType = new SystemExportTaskType();
        systemExportDefinitionType.setName(name);
        systemExportDefinitionType.setDescription(description);
        return systemExportDefinitionType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////


}