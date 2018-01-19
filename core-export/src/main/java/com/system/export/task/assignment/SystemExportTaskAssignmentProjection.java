package com.system.export.task.assignment;

import com.system.db.schema.table.SchemaTable;
import com.system.export.task.SystemExportTaskProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>SystemExportTaskAssignmentProjection</class> defines a projection
 * for all the fields of a SystemExportTaskAssignment
 *
 * @author Andrew
 */
@Projection(name = "export-system-export-task-assignment-all", types = SystemExportTaskAssignment.class)
public interface SystemExportTaskAssignmentProjection {

    public SystemExportTaskProjection getSystemExportTask();

    public SchemaTable getSchemaTable();

    public Integer getFkFieldId();

    public boolean isActive();

    public String getName();

    public String getDescription();

    public Integer getId();
}