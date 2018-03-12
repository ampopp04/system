/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.task;

import com.system.export.task.content.SystemExportTaskContentProjection;
import com.system.export.task.status.SystemExportTaskStatus;
import com.system.export.task.type.SystemExportTaskType;
import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;

/**
 * The <class>SystemExportTaskProjection</class> defines a projection
 * for all the fields of a SystemExportTask
 *
 * @author Andrew
 */
@Projection(name = "system-export-task-projection-all", types = SystemExportTask.class)
public interface SystemExportTaskProjection {

    public SystemExportTaskType getSystemExportTaskType();

    public SystemExportTaskStatus getSystemExportTaskStatus();

    public Collection<SystemExportTaskContentProjection> getSystemExportTaskContentCollection();

    public String getName();

    public String getDescription();

    public Integer getId();
}