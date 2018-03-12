/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.task.history;

import com.system.export.content.definition.SystemExportContentDefinitionProjection;
import com.system.export.generator.content.SystemExportGeneratorContentProjection;
import com.system.export.task.status.SystemExportTaskStatus;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>SystemExportTaskHistoryProjection</class> defines a projection
 * for all the fields of a SystemExportTaskHistory
 *
 * @author Andrew
 */
@Projection(name = "system-export-task-history-all", types = SystemExportTaskHistory.class)
public interface SystemExportTaskHistoryProjection {

    public SystemExportContentDefinitionProjection getSystemExportContentDefinition();

    public SystemExportGeneratorContentProjection getSystemExportGeneratorContent();

    public SystemExportTaskStatus getSystemExportTaskStatus();

    public String getDetails();

    public String getName();

    public String getDescription();

    public Integer getId();
}