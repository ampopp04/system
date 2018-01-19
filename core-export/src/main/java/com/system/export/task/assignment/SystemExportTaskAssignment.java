package com.system.export.task.assignment;

import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.table.SchemaTable;
import com.system.export.task.SystemExportTask;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportTaskAssignment</class> defines an assignment
 * between a {@link SystemExportTask} and an entity within the system {@link SchemaTable}
 * <p>
 * This means you can essentially assign an export to an entity within the system which allows us
 * to indirectly resolve all exports for a given entity.  By allowing the assignment to be a task to entity enables
 * the ability to have task execution schedules and content delivery executors.
 * <p>
 * This means a task can execute against various
 * entities within the system and automatically generate and deliver the content
 *
 * @author Andrew
 */
public class SystemExportTaskAssignment extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The task that will be executed to generate and deliver our content.
     */
    @ManyToOne
    @JoinColumn(name = "system_export_task_id")
    private SystemExportTask systemExportTask;

    /**
     * The entity table to assign this task to. This can be null is the task is to be assigned to all entities within the system.
     * It is generally advisable to set this.
     */
    @ManyToOne
    @JoinColumn(name = "schema_table_id")
    private SchemaTable schemaTable;

    /**
     * Defines that this task is assigned to a specific entity instance within the specified schema table.  This means that tasks can
     * be assigned globally (schemaTable == null) or a specific schema table (all entities within a table), or a specific entity within a specific table.
     */
    private Integer fkFieldId;

    /**
     * Defines whether this assignment is active and should be taken into consideration
     * when executing tasks derived from this assignment.
     */
    private boolean active;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTaskAssignment() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTask getSystemExportTask() {
        return systemExportTask;
    }

    public void setSystemExportTask(SystemExportTask systemExportTask) {
        this.systemExportTask = systemExportTask;
    }

    public SchemaTable getSchemaTable() {
        return schemaTable;
    }

    public void setSchemaTable(SchemaTable schemaTable) {
        this.schemaTable = schemaTable;
    }

    public Integer getFkFieldId() {
        return fkFieldId;
    }

    public void setFkFieldId(Integer fkFieldId) {
        this.fkFieldId = fkFieldId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}