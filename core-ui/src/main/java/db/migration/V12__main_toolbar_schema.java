package db.migration;


import com.system.bean.SystemBean;
import com.system.bean.definition.SystemBeanDefinition;
import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.bean.modifier.type.SystemBeanModifierType;
import com.system.bean.type.SystemBeanType;
import com.system.bean.variable.SystemBeanVariable;
import com.system.bean.variable.definition.SystemBeanVariableDefinition;
import com.system.bean.variable.definition.modifier.type.SystemBeanVariableDefinitionModifierType;
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
import com.system.ui.base.component.config.UiComponentConfig;
import com.system.ui.base.component.config.attribute.UiComponentConfigAttribute;
import com.system.ui.base.component.definition.UiComponentDefinition;
import com.system.ui.base.component.type.UiComponentType;
import com.system.util.collection.PairList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.ui.util.UiCreationUtils.*;
import static com.system.util.collection.CollectionUtils.*;
import static com.system.util.collection.Pair.newPair;
import static com.system.util.collection.PairList.newPairList;

/**
 * The <class>V5__main_toolbar_schema</class> defines the main toolbar schema
 *
 * @author Andrew
 */
public class V12__main_toolbar_schema extends BaseDataMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<UiComponentDefinition> uiComponentDefinitionRepository;

    @Autowired
    private NamedEntityRepository<UiComponentType> uiComponentTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getUiComponentDefinitionRepository().saveAll(getDataEntities());
    }

    private List<UiComponentDefinition> getDataEntities() {
        List<UiComponentDefinition> uiComponentDefinitionListList = newList();

        add(uiComponentDefinitionListList, generateHeaderDefinition());
        return uiComponentDefinitionListList;
    }

    private UiComponentDefinition generateHeaderDefinition() {
        return createUiComponentDefinition("System.view.application.main.header.Header", "", "Ext.toolbar.Toolbar", "appheader", "header", generateMainHeaderToolbar(),
                "System.view.application.main.header.HeaderController",
                "System.view.component.window.tab.grid.TabGridSystemWindow",
                "System.view.component.panel.tab.BaseSystemTabPanel",
                "System.util.component.GridColumnUtils",
                "System.util.system.WindowUtils");
    }


    private UiComponent generateMainHeaderToolbar() {
        UiComponent headerToolbar = createUiToolbar("Root Main Header Ui Toolbar", "This is the main toolbar displayed on the main page of the site.", getUiComponentTypeRepository().findByName(TOOLBAR_COMPONENT_TYPE));
        UiComponentConfig uiComponentConfig = createUiComponentConfig();
        headerToolbar.setUiComponentConfig(uiComponentConfig);

        UiComponentConfigAttribute uiComponentConfigAttribute = createUiComponentConfigAttribute(null, "ui", "'footer'");

        uiComponentConfig.addUiComponentConfigAttribute(uiComponentConfigAttribute);
        uiComponentConfig.addUiComponentConfigAttribute(createHeaderToolbarItemsAttribute());

        return headerToolbar;
    }

    private UiComponentConfigAttribute createHeaderToolbarItemsAttribute() {
        UiComponent uiComponent = createUiComponent("Main Header Toolbar Items Component", "This is the component under the items attribute.", getUiComponentTypeRepository().findByName(ITEMS_COMPONENT_TYPE));
        populateHeaderToolbarItemsElements(uiComponent);
        UiComponentConfigAttribute headerToolbarItemsAttribute = createUiComponentConfigAttribute(uiComponent, "items", null);

        return headerToolbarItemsAttribute;
    }

    private void populateHeaderToolbarItemsElements(UiComponent uiComponent) {
        UiComponentConfig uiComponentConfig = createUiComponentConfig();
        uiComponent.setUiComponentConfig(uiComponentConfig);
        configureHeaderToolbarItemsElementsConfig(uiComponentConfig);
    }

    private void configureHeaderToolbarItemsElementsConfig(UiComponentConfig uiComponentConfig) {
        uiComponentConfig.addUiComponentConfigAttribute(createAppHeaderRightNavAttribute());
        uiComponentConfig.addUiComponentConfigAttribute(createAppHeaderToolsAttribute());
        uiComponentConfig.addUiComponentConfigAttribute(createAppHeaderNavSpacerAttribute());
        uiComponentConfig.addUiComponentConfigAttribute(createAppHeaderLogoutAttribute());
    }

    private UiComponentConfigAttribute createAppHeaderRightNavAttribute() {
        return createUiCompontConfigAttributeByKeyValues("Main Header Logo Component", "This is a holder for the main header logo component", null,
                newPair(null, "'->'"));
    }

    private UiComponentConfigAttribute createAppHeaderToolsAttribute() {
        UiComponentConfigAttribute headerToolbarAttribute = createUiCompontConfigAttributeByKeyValues("Main Header Logo Component", "This is a holder for the main header logo component",
                null,
                newPair("text", "'Tools'"),
                newPair("iconCls", "'x-fa fa-cog'"));

        headerToolbarAttribute.getUiComponent().getUiComponentConfig().addUiComponentConfigAttribute(createToolbarMenu());

        return headerToolbarAttribute;
    }

    private UiComponentConfigAttribute createToolbarMenu() {
        UiComponentConfigAttribute adminToolbarMenu =
                createToolbarMenu("Main Header Toolbar Admin Menu", "This is the main header toolbars admin menu",
                        newPairList(
                                newPair("text", "'User Interface'"),
                                newPair("iconCls", "'x-fa fa-desktop'"),
                                newPair("handler", getUserInterfaceFunctionHandler())
                        ),
                        newPairList(
                                newPair(null, "'-'")
                        ),
                        newPairList(
                                newPair("text", "'Tasks'"),
                                newPair("iconCls", "'x-fa fa-tasks'")
                        ),
                        newPairList(
                                newPair("text", "'Beans'"),
                                newPair("iconCls", "'x-fa fa-leaf'"),
                                newPair(null, getSystemBeanToolbar())
                        ),
                        newPairList(
                                newPair("text", "'Exports'"),
                                newPair("iconCls", "'x-fa fa-file-text-o'"),
                                newPair(null, getSystemExportToolbar())
                        ),
                        newPairList(
                                newPair(null, "'-'")
                        ),
                        newPairList(
                                newPair("text", "'Schema'"),
                                newPair("iconCls", "'x-fa fa-database'"),
                                newPair("handler", getSchemaFunctionHandler())
                        ),
                        newPairList(
                                newPair("text", "'Security'"),
                                newPair("iconCls", "'x-fa fa-lock'"),
                                newPair("handler", getSecurityFunctionHandler())
                        ),
                        newPairList(
                                newPair(null, "'-'")
                        ),
                        newPairList(
                                newPair("text", "'Logging'"),
                                newPair("iconCls", "'x-fa fa-file'")
                        ),
                        newPairList(
                                newPair("text", "'Statistics'"),
                                newPair("iconCls", "'x-fa fa-line-chart'")
                        ));

        UiComponentConfigAttribute rootToolbarMenu =
                createToolbarMenu("Main Header Toolbar Menu", "This is the main header toolbars root menu", newPairList(
                        newPair("text", "'Administration'"),
                        newPair("iconCls", "'x-fa fa-wrench'"),
                        newPair(null, adminToolbarMenu)));

        return rootToolbarMenu;
    }

    private UiComponentConfigAttribute getSystemBeanToolbar() {
        return
                createToolbarMenu("System Bean Toolbar Menu", "The system bean toolbar menu",
                        newPairList(
                                newPair("text", "'Instances'"),
                                newPair("iconCls", "'x-fa fa-leaf'"),
                                newPair("handler", getBeanInstanceFunctionHandler())
                        ),
                        newPairList(
                                newPair("text", "'Variables'"),
                                newPair("iconCls", "'x-fa fa-leaf'"),
                                newPair("handler", getBeanVariableFunctionHandler())
                        ),
                        newPairList(
                                newPair(null, "'-'")
                        ),
                        newPairList(
                                newPair("text", "'Types'"),
                                newPair("iconCls", "'x-fa fa-leaf'"),
                                newPair("handler", getBeanTypeFunctionHandler())
                        ));
    }

    private UiComponentConfigAttribute getSystemExportToolbar() {
        return
                createToolbarMenu("System Export Toolbar Menu", "The system export toolbar menu",

                        newPairList(
                                newPair("text", "'Tasks'"),
                                newPair("iconCls", "'x-fa fa-file-code-o'"),
                                newPair("handler", getExportTaskFunctionHandler())
                        ),
                        newPairList(
                                newPair(null, "'-'")
                        ),
                        newPairList(
                                newPair("text", "'Templates'"),
                                newPair("iconCls", "'x-fa fa-file-code-o'"),
                                newPair("handler", getExportTemplateFunctionHandler())
                        ),
                        newPairList(
                                newPair(null, "'-'")
                        ),
                        newPairList(
                                newPair("text", "'Exports'"),
                                newPair("iconCls", "'x-fa fa-file-code-o'"),
                                newPair("handler", getExportFunctionHandler())
                        ),
                        newPairList(
                                newPair("text", "'Destinations'"),
                                newPair("iconCls", "'x-fa fa-file-code-o'"),
                                newPair("handler", getExportDestinationsFunctionHandler())
                        ),
                        newPairList(
                                newPair(null, "'-'")
                        ),
                        newPairList(
                                newPair("text", "'History'"),
                                newPair("iconCls", "'x-fa fa-file-code-o'"),
                                newPair("handler", getExportHistoryFunctionHandler())
                        ),
                        newPairList(
                                newPair(null, "'-'")
                        ),
                        newPairList(
                                newPair("text", "'Content'"),
                                newPair("iconCls", "'x-fa fa-file-code-o'"),
                                newPair("handler", getExportContentFunctionHandler())
                        ),
                        newPairList(
                                newPair("text", "'Generators'"),
                                newPair("iconCls", "'x-fa fa-file-code-o'"),
                                newPair("handler", getExportGeneratorsFunctionHandler())
                        )
                );
    }

    private String getSchemaFunctionHandler() {
        return createSchemaTabGridSystemWindow("System Schema", SchemaTable.class, SchemaTableColumn.class, SchemaTableColumnRelationship.class, SchemaDataType.class);
    }

    private String getUserInterfaceFunctionHandler() {
        return createSchemaTabGridSystemWindow("User Interface", UiComponentDefinition.class, UiComponent.class, UiComponentConfig.class, UiComponentConfigAttribute.class, UiComponentType.class);
    }

    private String getSecurityFunctionHandler() {
        return createSchemaTabGridSystemWindow("System Security", SystemSecurityUser.class, SystemSecurityPrivilege.class, SystemSecurityRole.class);
    }

    /**
     * System Export Handlers - Start (Tasks, Templates, Exports, Destinations, History, Content, Generators)
     */


    private String getExportTaskFunctionHandler() {
        return createSchemaTabGridSystemWindow("Tasks", SystemExportTaskStatus.class, SystemExportTask.class,
                SystemExportTaskContent.class,
                SystemExportTaskAssignment.class);
    }

    private String getExportTemplateFunctionHandler() {
        return createSchemaTabGridSystemWindow("Templates", SystemExportTemplate.class, SystemExportTemplateType.class);
    }

    private String getExportFunctionHandler() {
        return createSchemaTabGridSystemWindow("Exports", SystemExportFileType.class,
                SystemExportDefinitionType.class, SystemExportDefinition.class, SystemExport.class,
                SystemExportVariableMapping.class);
    }

    private String getExportDestinationsFunctionHandler() {
        return createSchemaTabGridSystemWindow("Destinations", SystemExportDestinationType.class, SystemExportDestination.class);
    }

    private String getExportHistoryFunctionHandler() {
        return createSchemaTabGridSystemWindow("Task History", SystemExportTaskHistory.class);
    }

    private String getExportContentFunctionHandler() {
        return createSchemaTabGridSystemWindow("Generated Content", SystemExportContentDefinition.class, SystemExportGeneratorContent.class);
    }

    private String getExportGeneratorsFunctionHandler() {
        return createSchemaTabGridSystemWindow("Export Generators", SystemExportGeneratorType.class, SystemExportGenerator.class);
    }

    /**
     * System Export Handlers - End
     */

    private String getBeanInstanceFunctionHandler() {
        return createSchemaTabGridSystemWindow("System Bean", SystemBean.class, SystemBeanType.class, SystemBeanDefinition.class);
    }

    private String getBeanVariableFunctionHandler() {
        return createSchemaTabGridSystemWindow("System Bean", SystemBeanVariableDefinition.class, SystemBeanVariable.class);
    }

    private String getBeanTypeFunctionHandler() {
        return createSchemaTabGridSystemWindow("Bean Type", SystemBeanDefinitionType.class,
                SystemBeanModifierType.class,
                SystemBeanVariableDefinitionModifierType.class);
    }


    private UiComponentConfigAttribute createToolbarMenu(String name, String description, PairList<String, ?>... pairList) {
        UiComponent rootMenuComponent = createUiComponent(name, description, getUiComponentTypeRepository().findByName(TOOLBAR_MENU_COMPONENT_TYPE));
        UiComponent rootMenuItemsComponent = createUiComponent(name + " Items", null, getUiComponentTypeRepository().findByName(ITEMS_COMPONENT_TYPE));

        UiComponentConfigAttribute rootMenuAttribute = createMenuAttribute(rootMenuComponent);
        rootMenuComponent.getUiComponentConfig().addUiComponentConfigAttribute(createItemsAttribute(rootMenuItemsComponent));

        UiComponentConfig rootMenuItemsComponentConfig = rootMenuItemsComponent.getUiComponentConfig();
        iterate(iterable(pairList), (pairs) -> {
            UiComponent pairComponent = createUiComponent(null, null, null);
            rootMenuItemsComponentConfig.addUiComponentConfigAttribute(createUiComponentConfigAttribute(pairComponent, null, null));

            iterate(iterable(pairs), (pair) -> {
                pairComponent.getUiComponentConfig()
                        .addUiComponentConfigAttribute(createUiComponentConfigAttributeForPair(pair));
            });
        });

        return rootMenuAttribute;
    }

    private UiComponentConfigAttribute createAppHeaderNavSpacerAttribute() {
        return createUiCompontConfigAttributeByKeyValues("Main Header Logo Component", "This is a holder for the main header logo component",
                null,
                newPair(null, "'-'"));
    }

    private UiComponentConfigAttribute createAppHeaderLogoutAttribute() {
        return createUiCompontConfigAttributeByKeyValues("Main Header Logo Component", "This is a holder for the main header logo component",
                null,
                newPair("itemId", "'logout'"),
                newPair("iconCls", "'x-fa fa-sign-out'"),
                newPair("text", "'Logout'"),
                newPair("reference", "'logout'"),
                newPair("listeners", "{click: 'onLogout'}"));
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<UiComponentDefinition> getUiComponentDefinitionRepository() {
        return uiComponentDefinitionRepository;
    }

    public void setUiComponentDefinitionRepository(NamedEntityRepository<UiComponentDefinition> uiComponentDefinitionRepository) {
        this.uiComponentDefinitionRepository = uiComponentDefinitionRepository;
    }

    public NamedEntityRepository<UiComponentType> getUiComponentTypeRepository() {
        return uiComponentTypeRepository;
    }

    public void setUiComponentTypeRepository(NamedEntityRepository<UiComponentType> uiComponentTypeRepository) {
        this.uiComponentTypeRepository = uiComponentTypeRepository;
    }
}