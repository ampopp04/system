package db.migration;


import com.system.bean.SystemBean;
import com.system.bean.definition.SystemBeanDefinition;
import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.bean.modifier.type.SystemBeanModifierType;
import com.system.bean.type.SystemBeanType;
import com.system.bean.variable.SystemBeanVariable;
import com.system.bean.variable.definition.SystemBeanVariableDefinition;
import com.system.bean.variable.definition.modifier.type.SystemBeanVariableDefinitionModifierType;
import com.system.db.entity.base.BaseEntity;
import com.system.db.migration.data.BaseDataMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.datatype.SchemaDataType;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.SchemaTableColumn;
import com.system.db.schema.table.column.relationship.SchemaTableColumnRelationship;
import com.system.export.SystemExport;
import com.system.export.content.definition.SystemExportContentDefinition;
import com.system.export.definition.SystemExportDefinition;
import com.system.export.definition.type.SystemExportDefinitionType;
import com.system.export.destination.SystemExportDestination;
import com.system.export.destination.type.SystemExportDestinationType;
import com.system.export.file.type.SystemExportFileType;
import com.system.export.generator.SystemExportGenerator;
import com.system.export.generator.content.SystemExportGeneratorContent;
import com.system.export.generator.type.SystemExportGeneratorType;
import com.system.export.task.SystemExportTask;
import com.system.export.task.assignment.SystemExportTaskAssignment;
import com.system.export.task.content.SystemExportTaskContent;
import com.system.export.task.history.SystemExportTaskHistory;
import com.system.export.task.status.SystemExportTaskStatus;
import com.system.export.template.SystemExportTemplate;
import com.system.export.template.type.SystemExportTemplateType;
import com.system.export.variable.SystemExportVariableMapping;
import com.system.security.privilege.SystemSecurityPrivilege;
import com.system.security.role.SystemSecurityRole;
import com.system.security.user.SystemSecurityUser;
import com.system.ui.base.component.UiComponent;
import com.system.ui.base.component.assignment.UiComponentAssignment;
import com.system.ui.base.component.config.UiComponentConfig;
import com.system.ui.base.component.config.attribute.UiComponentConfigAttribute;
import com.system.ui.base.component.definition.UiComponentDefinition;
import com.system.ui.base.component.type.UiComponentType;
import com.system.util.collection.Pair;
import com.system.util.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.system.ui.util.UiCreationUtils.PANEL_COMPONENT_TYPE;
import static com.system.ui.util.UiCreationUtils.createUiCompontConfigAttributeByKeyValues;
import static com.system.util.collection.CollectionUtils.*;
import static com.system.util.collection.Pair.newPair;

/**
 * The <class>V6__main_toolbar_assignments</class> defines the main toolbar ui component assignments
 *
 * @author Andrew
 */
public class V13__main_toolbar_assignments extends BaseDataMigration {

    private static final Map<Class<? extends BaseEntity>, String> entityIconMap;

    static {
        Map<Class<? extends BaseEntity>, String> iconMap = new HashMap<>();
        iconMap.put(SchemaTable.class, "x-fa fa-database");
        entityIconMap = Collections.unmodifiableMap(iconMap);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SchemaTable> schemaTableRepository;

    @Autowired
    private NamedEntityRepository<UiComponentType> uiComponentTypeRepository;

    @Autowired
    private NamedEntityRepository<UiComponentAssignment> uiComponentAssignmentRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getUiComponentAssignmentRepository().saveAll(getUiComponentAssignmentList(
                SchemaDataType.class, SchemaTable.class, SchemaTableColumn.class, SchemaTableColumnRelationship.class));

        getUiComponentAssignmentRepository().saveAll(getUiComponentAssignmentList(
                SystemBeanDefinitionType.class, SystemBeanDefinition.class, SystemBeanType.class,
                SystemBeanModifierType.class,
                SystemBeanVariableDefinition.class, SystemBeanVariableDefinitionModifierType.class,
                SystemBean.class, SystemBeanVariable.class));

        getUiComponentAssignmentRepository().saveAll(getUiComponentAssignmentList(
                SystemSecurityUser.class, SystemSecurityPrivilege.class, SystemSecurityRole.class));

        getUiComponentAssignmentRepository().saveAll(getUiComponentAssignmentList(UiComponentType.class,
                UiComponentDefinition.class, UiComponent.class,
                UiComponentConfigAttribute.class, UiComponentConfig.class,
                UiComponentAssignment.class));

        getUiComponentAssignmentRepository().saveAll(getUiComponentAssignmentList(
                SystemExportTaskStatus.class, SystemExportTask.class,
                SystemExportTaskContent.class,
                SystemExportTaskAssignment.class, SystemExportTemplate.class, SystemExportTemplateType.class, SystemExportFileType.class,
                SystemExportDefinitionType.class, SystemExportDefinition.class, SystemExport.class,
                SystemExportVariableMapping.class, SystemExportDestinationType.class, SystemExportDestination.class, SystemExportTaskHistory.class, SystemExportContentDefinition.class, SystemExportGeneratorContent.class
                , SystemExportGeneratorType.class, SystemExportGenerator.class
        ));
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                      Ui Component Assignments                                     //////////
    //////////////////////////////////////////////////////////////////////

    private List<UiComponentAssignment> getUiComponentAssignmentList(Class<? extends BaseEntity>... entityClasses) {
        List<UiComponentAssignment> assignmentList = newList();
        UiComponentType panelComponentType = getUiComponentTypeRepository().findByName(PANEL_COMPONENT_TYPE);

        iterate(iterable(entityClasses), (entityClass) -> {
            assignmentList.add(
                    assignComponentToSchemaTable(entityClass.getSimpleName(),
                            createUiCompontConfigAttributeByKeyValues(
                                    StringUtils.addSpaceOnCapitialLetters(entityClass.getSimpleName()), "This component is the grid panel representing the " + entityClass.getSimpleName() + " table.", panelComponentType,
                                    getDefaultEntityPairList(entityClass))
                                    .getUiComponent()));
        });

        return assignmentList;
    }

    private Pair<String, String>[] getDefaultEntityPairList(Class<? extends BaseEntity> entityClass) {
        List<Pair<String, String>> pairList = asList(
                newPair("title", "'" + StringUtils.addSpaceOnCapitialLetters(entityClass.getSimpleName()) + "'"),
                newPair("modelName", "'" + getPluralTable(entityClass.getSimpleName()) + "'"));

        if (entityIconMap.containsKey(entityClass)) {
            pairList.add(newPair("iconCls", "'" + entityIconMap.get(entityClass) + "'"));
        }

        Pair<String, String>[] pairs = new Pair[pairList.size()];
        pairs = pairList.toArray(pairs);

        return pairs;
    }

    private String getPluralTable(String tableName) {
        if (tableName.endsWith("History")) {
            return tableName.replace("History", "Histories");
        } else if (tableName.endsWith("Status")) {
            return tableName + "es";
        }
        return tableName + "s";
    }

    private UiComponentAssignment assignComponentToSchemaTable(String tableName, UiComponent uiComponent) {
        UiComponentAssignment uiComponentAssignment = new UiComponentAssignment();
        SchemaTable schemaTable = getSchemaTableRepository().findByName(tableName);
        uiComponentAssignment.setSchemaTable(schemaTable);
        uiComponentAssignment.setFkFieldId(schemaTable.getId());
        uiComponentAssignment.setUiComponent(uiComponent);
        return uiComponentAssignment;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<UiComponentType> getUiComponentTypeRepository() {
        return uiComponentTypeRepository;
    }

    public void setUiComponentTypeRepository(NamedEntityRepository<UiComponentType> uiComponentTypeRepository) {
        this.uiComponentTypeRepository = uiComponentTypeRepository;
    }

    public NamedEntityRepository<SchemaTable> getSchemaTableRepository() {
        return schemaTableRepository;
    }

    public void setSchemaTableRepository(NamedEntityRepository<SchemaTable> schemaTableRepository) {
        this.schemaTableRepository = schemaTableRepository;
    }

    public NamedEntityRepository<UiComponentAssignment> getUiComponentAssignmentRepository() {
        return uiComponentAssignmentRepository;
    }

    public void setUiComponentAssignmentRepository(NamedEntityRepository<UiComponentAssignment> uiComponentAssignmentRepository) {
        this.uiComponentAssignmentRepository = uiComponentAssignmentRepository;
    }
}