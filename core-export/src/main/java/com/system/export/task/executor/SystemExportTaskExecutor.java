package com.system.export.task.executor;

import com.system.bean.SystemBean;
import com.system.bean.service.SystemBeanService;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.export.content.definition.SystemExportContentDefinition;
import com.system.export.generator.content.SystemExportGeneratorContent;
import com.system.export.generator.executor.SystemExportGeneratorExecutor;
import com.system.export.task.SystemExportTask;
import com.system.export.task.content.SystemExportTaskContent;
import com.system.export.task.history.SystemExportTaskHistory;
import com.system.export.task.status.SystemExportTaskStatus;
import com.system.inversion.component.InversionComponent;
import com.system.util.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static com.system.util.collection.CollectionUtils.*;

/**
 * The <class>SystemExportTaskExecutor</class> manages
 * and executes Tasks
 *
 * @author Andrew
 */
@InversionComponent
public class SystemExportTaskExecutor {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private SystemBeanService systemBeanService;

    @Autowired
    private NamedEntityRepository<SystemExportTaskHistory> systemExportTaskHistoryRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTaskExecutor() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Task Execution                                               //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Execute the given task against the provided dataObject which will usually be a specific table row entity.
     * The task will execute and this will synchronously produce a execution result encapsulated by the
     * returned task history object.
     *
     * @param task       - The task to execute
     * @param dataObject - The object or entity that we will use and apply to the task to generate our export
     * @return - The history of the run and all information related to this task execution along with the export content that was generated
     */
    public SystemExportTaskHistory executeTask(SystemExportTask task, Object dataObject) {
        SystemExportGeneratorContent generatedContent = null;
        String details = null;

        try {
            generatedContent = generateContent(task, dataObject);
        } catch (Exception e) {
            details = e.getLocalizedMessage();
            e.printStackTrace();
        }

        return getSystemExportTaskHistoryRepository().save(createSystemExportTaskHistory(task, generatedContent == null ? null : generatedContent.getSystemExportContentDefinition(), generatedContent, task.getSystemExportTaskStatus(), details));
    }

    private SystemExportGeneratorContent generateContent(SystemExportTask task, Object dataObject) {
        Collection<SystemExportTaskContent> systemExportTaskContentCollection = task.getSystemExportTaskContentCollection();

        //Currently only support a single task content definition per task
        ValidationUtils.assertTrue(!empty(systemExportTaskContentCollection));
        ValidationUtils.assertEqualTo(size(systemExportTaskContentCollection), 1);

        SystemExportTaskContent taskContent = firstElement(systemExportTaskContentCollection);

        SystemExportGeneratorExecutor operationExecutor = validateAndGetTaskContentGeneratorExecutor(taskContent);

        return operationExecutor.generate(taskContent.getSystemExportContentDefinition(), dataObject);
    }

    private SystemExportTaskHistory createSystemExportTaskHistory(SystemExportTask task, SystemExportContentDefinition systemExportContentDefinition, SystemExportGeneratorContent systemExportGeneratorContent, SystemExportTaskStatus systemExportTaskStatus, String details) {
        SystemExportTaskHistory taskHistory = new SystemExportTaskHistory();
        taskHistory.setName(task.getName());
        taskHistory.setDescription(task.getDescription());
        taskHistory.setSystemExportContentDefinition(systemExportContentDefinition);
        taskHistory.setSystemExportGeneratorContent(systemExportGeneratorContent);
        taskHistory.setSystemExportTaskStatus(systemExportTaskStatus);
        taskHistory.setDetails(details);
        return taskHistory;
    }

    private SystemExportGeneratorExecutor validateAndGetTaskContentGeneratorExecutor(SystemExportTaskContent content) {

        if (content == null) {

        } else if (content.getSystemExportContentDefinition() == null) {

        } else if (content.getSystemExportContentDefinition().getSystemExportGenerator() == null) {

        } else if (content.getSystemExportContentDefinition().getSystemExportGenerator().getSystemBean() == null) {

        }

        SystemBean exportGeneratorBean = content.getSystemExportContentDefinition().getSystemExportGenerator().getSystemBean();
        return (SystemExportGeneratorExecutor) getSystemBeanService().getSystemBeanInstance(exportGeneratorBean);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanService getSystemBeanService() {
        return systemBeanService;
    }

    public void setSystemBeanService(SystemBeanService systemBeanService) {
        this.systemBeanService = systemBeanService;
    }

    public NamedEntityRepository<SystemExportTaskHistory> getSystemExportTaskHistoryRepository() {
        return systemExportTaskHistoryRepository;
    }

    public void setSystemExportTaskHistoryRepository(NamedEntityRepository<SystemExportTaskHistory> systemExportTaskHistoryRepository) {
        this.systemExportTaskHistoryRepository = systemExportTaskHistoryRepository;
    }
}