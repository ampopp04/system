/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.task;

import com.system.db.entity.named.NamedEntity;
import com.system.export.task.content.SystemExportTaskContent;
import com.system.export.task.status.SystemExportTaskStatus;
import com.system.export.task.type.SystemExportTaskType;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * The <class>SystemExportTask</class> defines the meta-data surrounding a task
 * and the specifics on when it should be executed.
 *
 * @author Andrew
 */
public class SystemExportTask extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_export_task_type_id")
    private SystemExportTaskType systemExportTaskType;

    @ManyToOne
    @JoinColumn(name = "system_export_task_status_id")
    private SystemExportTaskStatus systemExportTaskStatus;

    /**
     * The list of content this task can generate and deliver
     */
    @OneToMany(mappedBy = "systemExportTask", fetch = FetchType.EAGER)
    private Collection<SystemExportTaskContent> systemExportTaskContentCollection;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTask() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////
    
    public SystemExportTaskType getSystemExportTaskType() {
        return systemExportTaskType;
    }

    public void setSystemExportTaskType(SystemExportTaskType systemExportTaskType) {
        this.systemExportTaskType = systemExportTaskType;
    }

    public SystemExportTaskStatus getSystemExportTaskStatus() {
        return systemExportTaskStatus;
    }

    public void setSystemExportTaskStatus(SystemExportTaskStatus systemExportTaskStatus) {
        this.systemExportTaskStatus = systemExportTaskStatus;
    }

    public Collection<SystemExportTaskContent> getSystemExportTaskContentCollection() {
        return systemExportTaskContentCollection;
    }

    public void setSystemExportTaskContentCollection(Collection<SystemExportTaskContent> systemExportTaskContentCollection) {
        this.systemExportTaskContentCollection = systemExportTaskContentCollection;
    }
}