package db.migration;


import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.export.generator.content.SystemExportGeneratorContent;
import com.system.export.task.SystemExportTask;
import com.system.export.task.assignment.SystemExportTaskAssignment;
import com.system.export.task.content.SystemExportTaskContent;
import com.system.export.task.status.SystemExportTaskStatus;
import com.system.export.task.type.SystemExportTaskType;
import com.system.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V9__task_schema</class> defines the initial
 * export task schema. An export task is a piece of work that is to
 * be executed for generating and creating export content in various ways.
 * <p>
 * It can specifying times, schedules, or conditions for when to execute a specific
 * export generation or content delivery.
 * <p>
 * Ex. We can specify a task for delivering a specific report to an specific email destination every wednesday for instance.
 * <p>
 * This also defines the status of the task along with assigning an export task to a specific entity field within the system.
 * These assignments can more simply be thought of assigning a report or any export to some entity within the system.
 * <p>
 * This might be a job spreadsheet report that is assigned to the Job entity table. That way in the UI we can show all the reports for
 * that entity and via it's related meta-data more finely show by department and such.
 * <p>
 * A task has a list of content that it needs to generate. This content is defined by the {@link SystemExportTaskContent} which
 * associates a {@link SystemExportGeneratorContent} to a specific {@link SystemExportTask}. This allows a task to hold a list of content that might
 * be a list of reports to generate or possibly a list of content that is to be combined into a single report.
 *
 * @author Andrew
 */
public class V9__task_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SystemExportTaskStatus> systemExportTaskStatusRepository;

    @Autowired
    private NamedEntityRepository<SystemExportTaskType> systemExportTaskTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return CollectionUtils.asList(
                SystemExportTaskType.class, SystemExportTaskStatus.class, SystemExportTask.class,
                SystemExportTaskContent.class,
                SystemExportTaskAssignment.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getSystemExportTaskTypeRepository().saveAll(getSystemExportTaskTypeList());
        getSystemExportTaskStatusRepository().saveAll(getSystemExportTaskStatusList());
    }

    public List<SystemExportTaskStatus> getSystemExportTaskStatusList() {
        return asList(
                SystemExportTaskStatus.newInstance("NEW", "A new task."),
                SystemExportTaskStatus.newInstance("PENDING", "A task pending some condition."),
                SystemExportTaskStatus.newInstance("RUNNING", "A task running execution."),
                SystemExportTaskStatus.newInstance("COMPLETE", "A completed task."),
                SystemExportTaskStatus.newInstance("FAILED", "A task that has failed."),

                SystemExportTaskStatus.newInstance("NONE", "A task without a status."),

                SystemExportTaskStatus.newInstance("WAITING_FOR_RETRY", "A task that failed but is going to try again."),
                SystemExportTaskStatus.newInstance("COMPLETE_WITH_FAILURES", "A task where portions of it failed but some completed."),

                SystemExportTaskStatus.newInstance("FAILED_ERROR", "A task that failed to execute to any sort of known completion."),
                SystemExportTaskStatus.newInstance("FAILED_NOT_READY", "A task that failed because some condition wasn't ready.")
        );
    }

    public List<SystemExportTaskType> getSystemExportTaskTypeList() {
        return asList(
                SystemExportTaskType.newInstance("Project Management", "Project management specific exports and controls."),
                SystemExportTaskType.newInstance("Accounting", "Accounting specific exports and controls."),
                SystemExportTaskType.newInstance("Sales", "Sales specific exports and controls."),
                SystemExportTaskType.newInstance("Engineering", "Engineering specific exports and controls."),
                SystemExportTaskType.newInstance("Marketing", "Marketing specific exports and controls.")
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<SystemExportTaskStatus> getSystemExportTaskStatusRepository() {
        return systemExportTaskStatusRepository;
    }

    public void setSystemExportTaskStatusRepository(NamedEntityRepository<SystemExportTaskStatus> systemExportTaskStatusRepository) {
        this.systemExportTaskStatusRepository = systemExportTaskStatusRepository;
    }

    public NamedEntityRepository<SystemExportTaskType> getSystemExportTaskTypeRepository() {
        return systemExportTaskTypeRepository;
    }

    public void setSystemExportTaskTypeRepository(NamedEntityRepository<SystemExportTaskType> systemExportTaskTypeRepository) {
        this.systemExportTaskTypeRepository = systemExportTaskTypeRepository;
    }
}