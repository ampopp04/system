package com.system.export.template.upload;

import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.table.SchemaTable;
import com.system.export.SystemExport;
import com.system.export.content.definition.SystemExportContentDefinition;
import com.system.export.definition.SystemExportDefinition;
import com.system.export.definition.type.SystemExportDefinitionType;
import com.system.export.destination.SystemExportDestination;
import com.system.export.file.type.SystemExportFileType;
import com.system.export.generator.SystemExportGenerator;
import com.system.export.task.SystemExportTask;
import com.system.export.task.assignment.SystemExportTaskAssignment;
import com.system.export.task.content.SystemExportTaskContent;
import com.system.export.task.status.SystemExportTaskStatus;
import com.system.export.task.type.SystemExportTaskType;
import com.system.export.template.SystemExportTemplate;
import com.system.export.template.type.SystemExportTemplateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

/**
 * The <class>SystemExportTemplateUploadController</class> defines
 * the ability to upload export template files
 * <p>
 * This makes it easy for outside individuals to upload templates with custom settings
 * rather than having to create all of the various entities to support a regular UI based
 * instant Report generation.
 *
 * @author Andrew
 */
@RepositoryRestController
public class SystemExportTemplateUploadController {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Constants                                                        //////////
    //////////////////////////////////////////////////////////////////////

    private static final String BASE_MAPPING = "/{repository}/uploadTemplate";
    private static final String ACCEPT_HEADER = "Accept";

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Repositories                                                      //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SchemaTable> schemaTableRepository;

    @Autowired
    private NamedEntityRepository<SystemExportTemplateType> systemExportTemplateTypeRepository;
    @Autowired
    private NamedEntityRepository<SystemExportTemplate> systemExportTemplateRepository;

    @Autowired
    private NamedEntityRepository<SystemExportDefinition> systemExportDefinitionRepository;
    @Autowired
    private NamedEntityRepository<SystemExportDefinitionType> systemExportDefinitionTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemExport> systemExportRepository;

    @Autowired
    private NamedEntityRepository<SystemExportContentDefinition> systemExportContentDefinitionRepository;
    @Autowired
    private NamedEntityRepository<SystemExportGenerator> systemExportGeneratorRepository;

    @Autowired
    private NamedEntityRepository<SystemExportTaskStatus> systemExportTaskStatusRepository;
    @Autowired
    private NamedEntityRepository<SystemExportTask> systemExportTaskRepository;

    @Autowired
    private NamedEntityRepository<SystemExportDestination> systemExportDestinationRepository;
    @Autowired
    private NamedEntityRepository<SystemExportTaskContent> systemExportTaskContentRepository;

    @Autowired
    private NamedEntityRepository<SystemExportTaskAssignment> systemExportTaskAssignmentRepository;


    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTemplateUploadController() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                           Controller Methods                                                //////////
    //////////////////////////////////////////////////////////////////////

    @ResponseBody
    @RequestMapping(value = BASE_MAPPING, method = RequestMethod.POST)
    public ResponseEntity<String> postCollectionResource(RootResourceInformation resourceInformation,
                                                         @RequestParam("templateFile") MultipartFile templateFile,
                                                         @RequestParam("systemExportTaskType") SystemExportTaskType systemExportTaskType,
                                                         @RequestParam("systemExportFileType") SystemExportFileType systemExportFileType,
                                                         @RequestParam("name") String name,
                                                         @RequestParam("description") String description,
                                                         PersistentEntityResourceAssembler assembler, @RequestHeader(value = ACCEPT_HEADER, required = false) String acceptHeader) throws HttpRequestMethodNotSupportedException, IOException {

        createDefaultSystemExportTaskAssignment(
                templateFile, systemExportTaskType, systemExportFileType, name, description,
                getSchemaTableRepository().findByName(resourceInformation.getDomainType().getSimpleName())
        );

        return ResponseEntity.ok("{success:true}");
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                     Export Creation Methods                                         //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Upload a Template - File
     * -> Create new SystemExportTemplate of type ‘Report’ with the provided name and description and file
     * <p>
     * The upload window will have a combo drop down that lists all of the SystemExportDefinitionTypes (Project Management, Accounting, Sales, etc)
     * <p>
     * -> Create a SystemExportDefinition with the provided upload Name and Description and SystemExportDefinitionType
     * <p>
     * The upload window will have a combo drop down that lists all of the SystemExportFileTypes
     * <p>
     * -> Create a SystemExport using the provided name and description, the previously created SystemExportDefinition and the user provided SystemExportFileType
     * <p>
     * -> Create a SystemExportContentDefinition from the provided SystemExportTemplate, using the provided name and description, select the default SystemExportGenerator (“Standard Report Generator”) and set the previously created SystemExport
     * <p>
     * -> Create a SystemExportTask with the provided name and description along with setting the status to a default of “NEW”
     * <p>
     * -> Create a SystemExportTaskContent using the provided name and description, using the previously created SystemExportContentDefinition, SystemExportTask, and a SystemExportDestination of a default of (“User Interface”)
     * <p>
     * -> Create a SystemExportTaskAssignment using the provided name and description, and using the previously created SystemExportTask, set Active = true by default, set the SchemaTable based off of the repository that called the controller and leave fkFIeldId blank
     */
    @Transactional
    private SystemExportTaskAssignment createDefaultSystemExportTaskAssignment(MultipartFile templateFile, SystemExportTaskType systemExportTaskType, SystemExportFileType systemExportFileType, String name, String description, SchemaTable schemaTable) throws IOException {

        SystemExportTask systemExportTask = createSystemExportTask(name, description, systemExportTaskType);

        createSystemExportTaskContent(
                name,
                description,
                createSystemExportContentDefinition(
                        name,
                        description,
                        createSystemExportTemplate(
                                templateFile,
                                name,
                                description,
                                systemExportFileType
                        ),
                        createSystemExport(
                                name,
                                description,
                                createSystemExportDefinition(
                                        name,
                                        description,
                                        getSystemExportDefinitionTypeRepository().findByName(systemExportTaskType.getName())
                                ),
                                systemExportFileType
                        )
                ),
                systemExportTask
        );

        return createSystemExportTaskAssignment(name, description, systemExportTask, schemaTable);
    }

    /**
     * Create new SystemExportTemplate of type ‘Report’ with the provided name and description and file
     */
    private SystemExportTemplate createSystemExportTemplate(MultipartFile templateFile, String name, String description, SystemExportFileType systemExportFileType) throws IOException {
        SystemExportTemplate systemExportTemplate = new SystemExportTemplate();
        systemExportTemplate.setName(name);
        systemExportTemplate.setDescription(description);
        systemExportTemplate.setTemplateContent(templateFile.getBytes());
        systemExportTemplate.setSystemExportFileType(systemExportFileType);
        systemExportTemplate.setSystemExportTemplateType(getSystemExportTemplateTypeRepository().findByName("Report"));
        return getSystemExportTemplateRepository().save(systemExportTemplate);
    }

    /**
     * Create a SystemExportDefinition with the provided upload Name and Description and SystemExportDefinitionType
     */
    private SystemExportDefinition createSystemExportDefinition(String name, String description, SystemExportDefinitionType systemExportDefinitionType) {
        SystemExportDefinition systemExportDefinition = new SystemExportDefinition();
        systemExportDefinition.setName(name);
        systemExportDefinition.setDescription(description);
        systemExportDefinition.setSystemExportDefinitionType(systemExportDefinitionType);
        return getSystemExportDefinitionRepository().save(systemExportDefinition);
    }

    /**
     * Create a SystemExport using the provided name and description, the previously created SystemExportDefinition and the user provided SystemExportFileType
     */
    private SystemExport createSystemExport(String name, String description, SystemExportDefinition systemExportDefinition, SystemExportFileType systemExportFileType) {
        SystemExport systemExport = new SystemExport();
        systemExport.setName(name);
        systemExport.setDescription(description);
        systemExport.setSystemExportDefinition(systemExportDefinition);
        systemExport.setSystemExportFileType(systemExportFileType);
        return getSystemExportRepository().save(systemExport);
    }

    /**
     * Create a SystemExportContentDefinition from the provided SystemExportTemplate, using the provided name and description, select the default SystemExportGenerator (“Standard Report Generator”) and set the previously created SystemExport
     */
    private SystemExportContentDefinition createSystemExportContentDefinition(String name, String description, SystemExportTemplate systemExportTemplate, SystemExport systemExport) {
        SystemExportContentDefinition systemExportContentDefinition = new SystemExportContentDefinition();
        systemExportContentDefinition.setName(name);
        systemExportContentDefinition.setDescription(description);
        systemExportContentDefinition.setSystemExportTemplate(systemExportTemplate);
        systemExportContentDefinition.setSystemExport(systemExport);
        systemExportContentDefinition.setSystemExportGenerator(getSystemExportGeneratorRepository().findByName("Standard Report Generator"));
        return getSystemExportContentDefinitionRepository().save(systemExportContentDefinition);
    }

    /**
     * Create a SystemExportTask with the provided name and description along with setting the status to a default of “NEW”
     */
    private SystemExportTask createSystemExportTask(String name, String description, SystemExportTaskType systemExportTaskType) {
        SystemExportTask systemExportTask = new SystemExportTask();
        systemExportTask.setName(name);
        systemExportTask.setDescription(description);
        systemExportTask.setSystemExportTaskType(systemExportTaskType);
        systemExportTask.setSystemExportTaskStatus(getSystemExportTaskStatusRepository().findByName("NEW"));
        return getSystemExportTaskRepository().save(systemExportTask);
    }

    /**
     * Create a SystemExportTaskContent using the provided name and description, using the previously created SystemExportContentDefinition, SystemExportTask, and a SystemExportDestination of a default of (“User Interface”)
     */
    private SystemExportTaskContent createSystemExportTaskContent(String name, String description, SystemExportContentDefinition systemExportContentDefinition, SystemExportTask systemExportTask) {
        SystemExportTaskContent systemExportTaskContent = new SystemExportTaskContent();
        systemExportTaskContent.setName(name);
        systemExportTaskContent.setDescription(description);
        systemExportTaskContent.setSystemExportContentDefinition(systemExportContentDefinition);
        systemExportTaskContent.setSystemExportTask(systemExportTask);
        systemExportTaskContent.setSystemExportDestination(getSystemExportDestinationRepository().findByName("User Interface"));
        return getSystemExportTaskContentRepository().save(systemExportTaskContent);
    }

    /**
     * Create a SystemExportTaskAssignment using the provided name and description, and using the previously created SystemExportTask, set Active = true by default, set the SchemaTable based off of the repository that called the controller and leave fkFIeldId blank
     */
    private SystemExportTaskAssignment createSystemExportTaskAssignment(String name, String description, SystemExportTask systemExportTask, SchemaTable schemaTable) {
        SystemExportTaskAssignment systemExportTaskAssignment = new SystemExportTaskAssignment();
        systemExportTaskAssignment.setName(name);
        systemExportTaskAssignment.setDescription(description);
        systemExportTaskAssignment.setSystemExportTask(systemExportTask);
        systemExportTaskAssignment.setSchemaTable(schemaTable);
        systemExportTaskAssignment.setActive(true);
        return getSystemExportTaskAssignmentRepository().save(systemExportTaskAssignment);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<SchemaTable> getSchemaTableRepository() {
        return schemaTableRepository;
    }

    public void setSchemaTableRepository(NamedEntityRepository<SchemaTable> schemaTableRepository) {
        this.schemaTableRepository = schemaTableRepository;
    }

    public NamedEntityRepository<SystemExportTemplateType> getSystemExportTemplateTypeRepository() {
        return systemExportTemplateTypeRepository;
    }

    public void setSystemExportTemplateTypeRepository(NamedEntityRepository<SystemExportTemplateType> systemExportTemplateTypeRepository) {
        this.systemExportTemplateTypeRepository = systemExportTemplateTypeRepository;
    }

    public NamedEntityRepository<SystemExportTemplate> getSystemExportTemplateRepository() {
        return systemExportTemplateRepository;
    }

    public void setSystemExportTemplateRepository(NamedEntityRepository<SystemExportTemplate> systemExportTemplateRepository) {
        this.systemExportTemplateRepository = systemExportTemplateRepository;
    }

    public NamedEntityRepository<SystemExportDefinition> getSystemExportDefinitionRepository() {
        return systemExportDefinitionRepository;
    }

    public void setSystemExportDefinitionRepository(NamedEntityRepository<SystemExportDefinition> systemExportDefinitionRepository) {
        this.systemExportDefinitionRepository = systemExportDefinitionRepository;
    }

    public NamedEntityRepository<SystemExportDefinitionType> getSystemExportDefinitionTypeRepository() {
        return systemExportDefinitionTypeRepository;
    }

    public void setSystemExportDefinitionTypeRepository(NamedEntityRepository<SystemExportDefinitionType> systemExportDefinitionTypeRepository) {
        this.systemExportDefinitionTypeRepository = systemExportDefinitionTypeRepository;
    }

    public NamedEntityRepository<SystemExport> getSystemExportRepository() {
        return systemExportRepository;
    }

    public void setSystemExportRepository(NamedEntityRepository<SystemExport> systemExportRepository) {
        this.systemExportRepository = systemExportRepository;
    }

    public NamedEntityRepository<SystemExportContentDefinition> getSystemExportContentDefinitionRepository() {
        return systemExportContentDefinitionRepository;
    }

    public void setSystemExportContentDefinitionRepository(NamedEntityRepository<SystemExportContentDefinition> systemExportContentDefinitionRepository) {
        this.systemExportContentDefinitionRepository = systemExportContentDefinitionRepository;
    }

    public NamedEntityRepository<SystemExportGenerator> getSystemExportGeneratorRepository() {
        return systemExportGeneratorRepository;
    }

    public void setSystemExportGeneratorRepository(NamedEntityRepository<SystemExportGenerator> systemExportGeneratorRepository) {
        this.systemExportGeneratorRepository = systemExportGeneratorRepository;
    }

    public NamedEntityRepository<SystemExportTaskStatus> getSystemExportTaskStatusRepository() {
        return systemExportTaskStatusRepository;
    }

    public void setSystemExportTaskStatusRepository(NamedEntityRepository<SystemExportTaskStatus> systemExportTaskStatusRepository) {
        this.systemExportTaskStatusRepository = systemExportTaskStatusRepository;
    }

    public NamedEntityRepository<SystemExportTask> getSystemExportTaskRepository() {
        return systemExportTaskRepository;
    }

    public void setSystemExportTaskRepository(NamedEntityRepository<SystemExportTask> systemExportTaskRepository) {
        this.systemExportTaskRepository = systemExportTaskRepository;
    }

    public NamedEntityRepository<SystemExportDestination> getSystemExportDestinationRepository() {
        return systemExportDestinationRepository;
    }

    public void setSystemExportDestinationRepository(NamedEntityRepository<SystemExportDestination> systemExportDestinationRepository) {
        this.systemExportDestinationRepository = systemExportDestinationRepository;
    }

    public NamedEntityRepository<SystemExportTaskContent> getSystemExportTaskContentRepository() {
        return systemExportTaskContentRepository;
    }

    public void setSystemExportTaskContentRepository(NamedEntityRepository<SystemExportTaskContent> systemExportTaskContentRepository) {
        this.systemExportTaskContentRepository = systemExportTaskContentRepository;
    }

    public NamedEntityRepository<SystemExportTaskAssignment> getSystemExportTaskAssignmentRepository() {
        return systemExportTaskAssignmentRepository;
    }

    public void setSystemExportTaskAssignmentRepository(NamedEntityRepository<SystemExportTaskAssignment> systemExportTaskAssignmentRepository) {
        this.systemExportTaskAssignmentRepository = systemExportTaskAssignmentRepository;
    }
}