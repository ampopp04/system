package com.system.export.task.history;

import com.system.db.entity.named.NamedEntity;
import com.system.export.content.definition.SystemExportContentDefinition;
import com.system.export.generator.content.SystemExportGeneratorContent;
import com.system.export.task.SystemExportTask;
import com.system.export.task.assignment.SystemExportTaskAssignment;
import com.system.export.task.status.SystemExportTaskStatus;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportTaskHistory</class> defines
 * a container for any past information that was generated for a specific {@link SystemExportTaskAssignment}
 * {@link SystemExportTask} execution.
 * <p>
 * This table allows individuals to look back on past task executions to see their historical information.
 * <p>
 * This table holds the content definition as to how the task generated it's content and all of the meta-data surrounding that process
 * and it also holds the actual content object that holds the content that was generated via the task execution process.
 * <p>
 * It also holds a status to show what status the task had at the end and any errors/information reported.
 *
 * @author Andrew
 */
public class SystemExportTaskHistory extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Defines all of the meta-data surrounding how the export content was generated
     */
    @ManyToOne
    @JoinColumn(name = "system_export_content_definition_id")
    private SystemExportContentDefinition systemExportContentDefinition;

    /**
     * Actually is the content that was generated via the above systemExportContentDefinition meta-data
     * by an export generator
     */
    @ManyToOne
    @JoinColumn(name = "system_export_generator_content_id")
    private SystemExportGeneratorContent systemExportGeneratorContent;

    /**
     * Shows the final status that the task ended in after generating the content
     */
    @ManyToOne
    @JoinColumn(name = "system_export_task_status_id")
    private SystemExportTaskStatus systemExportTaskStatus;

    /**
     * An opportunity for this history object to explain any specific details about the task execution process.
     * This would generally be an ERROR message if applicable or information explaining what happened
     */
    private String details;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTaskHistory() {
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

    public SystemExportGeneratorContent getSystemExportGeneratorContent() {
        return systemExportGeneratorContent;
    }

    public void setSystemExportGeneratorContent(SystemExportGeneratorContent systemExportGeneratorContent) {
        this.systemExportGeneratorContent = systemExportGeneratorContent;
    }

    public SystemExportTaskStatus getSystemExportTaskStatus() {
        return systemExportTaskStatus;
    }

    public void setSystemExportTaskStatus(SystemExportTaskStatus systemExportTaskStatus) {
        this.systemExportTaskStatus = systemExportTaskStatus;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}