/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.task.status;

import com.system.db.entity.named.NamedEntity;

/**
 * The <class>SystemExportTaskStatus</class> defines
 * different states that a task can be in.
 *
 * @author Andrew
 */
public class SystemExportTaskStatus extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTaskStatus() {
    }

    public static SystemExportTaskStatus newInstance(String name, String description) {
        SystemExportTaskStatus systemExportTaskStatus = new SystemExportTaskStatus();
        systemExportTaskStatus.setName(name);
        systemExportTaskStatus.setDescription(description);
        return systemExportTaskStatus;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}