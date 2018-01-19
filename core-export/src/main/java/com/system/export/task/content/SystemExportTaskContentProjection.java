package com.system.export.task.content;

import com.system.export.content.definition.SystemExportContentDefinitionProjection;
import com.system.export.destination.SystemExportDestination;
import com.system.export.task.SystemExportTask;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>SystemExportTaskContentProjection</class> defines a projection
 * for all the fields of a SystemExportTaskContent
 *
 * @author Andrew
 */
@Projection(name = "system-export-task-content-all", types = SystemExportTaskContent.class)
public interface SystemExportTaskContentProjection {

    public SystemExportContentDefinitionProjection getSystemExportContentDefinition();

    public SystemExportDestination getSystemExportDestination();

    public SystemExportTask getSystemExportTask();

    public String getName();

    public String getDescription();

    public Integer getId();
}