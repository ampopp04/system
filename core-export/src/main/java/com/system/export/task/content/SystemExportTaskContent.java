package com.system.export.task.content;

import com.system.db.entity.named.NamedEntity;
import com.system.export.content.definition.SystemExportContentDefinition;
import com.system.export.destination.SystemExportDestination;
import com.system.export.task.SystemExportTask;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportTaskContent</class> defines an
 * association between {@link SystemExportContentDefinition}, {@link SystemExportTask} and a {@link SystemExportDestination}
 * <p>
 * This allows a task to hold many pieces of content that can be generated at once. This means that a single
 * task could potentially generate several reports or pull together several pieces of content together into a single report.
 * <p>
 * It is also possible that a task then can send this content to various destinations or combine them all together into one destination if
 * the destinations are found to be the same.
 *
 * @author Andrew
 */
public class SystemExportTaskContent extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The export content definition that defines how this content was actually generated
     * This includes the export meta-data regarding the specifics of export generation details
     * along with a template that was used to actually generate the export
     * along with specifying the actual generator that was used to produce the export content
     */
    @ManyToOne
    @JoinColumn(name = "system_export_content_definition_id")
    private SystemExportContentDefinition systemExportContentDefinition;

    @ManyToOne
    @JoinColumn(name = "system_export_destination_id")
    private SystemExportDestination systemExportDestination;

    @ManyToOne
    @JoinColumn(name = "system_export_task_id")
    private SystemExportTask systemExportTask;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTaskContent() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportContentDefinition getSystemExportContentDefinition() {
        return systemExportContentDefinition;
    }

    public void setSystemExportContentDefinition(SystemExportContentDefinition systemExportContentDefinition) {
        this.systemExportContentDefinition = systemExportContentDefinition;
    }

    public SystemExportDestination getSystemExportDestination() {
        return systemExportDestination;
    }

    public void setSystemExportDestination(SystemExportDestination systemExportDestination) {
        this.systemExportDestination = systemExportDestination;
    }

    public SystemExportTask getSystemExportTask() {
        return systemExportTask;
    }

    public void setSystemExportTask(SystemExportTask systemExportTask) {
        this.systemExportTask = systemExportTask;
    }
}