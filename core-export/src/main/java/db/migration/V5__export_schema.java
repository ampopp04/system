package db.migration;


import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.export.SystemExport;
import com.system.export.definition.SystemExportDefinition;
import com.system.export.definition.type.SystemExportDefinitionType;
import com.system.export.variable.SystemExportVariableMapping;
import com.system.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V5__export_schema</class> defines the initial
 * database migration schema for exports
 *
 * @author Andrew
 */
public class V5__export_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SystemExportDefinitionType> systemExportDefinitionTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return CollectionUtils.asList(
                SystemExportDefinitionType.class, SystemExportDefinition.class, SystemExport.class,
                SystemExportVariableMapping.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getSystemExportDefinitionTypeRepository().saveAll(getSystemExportDefinitionTypeList());
    }

    public List<SystemExportDefinitionType> getSystemExportDefinitionTypeList() {
        return asList(
                SystemExportDefinitionType.newInstance("Project Management", "Project management specific exports and controls."),
                SystemExportDefinitionType.newInstance("Accounting", "Accounting specific exports and controls."),
                SystemExportDefinitionType.newInstance("Sales", "Sales specific exports and controls."),
                SystemExportDefinitionType.newInstance("Engineering", "Engineering specific exports and controls."),
                SystemExportDefinitionType.newInstance("Marketing", "Marketing specific exports and controls.")
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<SystemExportDefinitionType> getSystemExportDefinitionTypeRepository() {
        return systemExportDefinitionTypeRepository;
    }

    public void setSystemExportDefinitionTypeRepository(NamedEntityRepository<SystemExportDefinitionType> systemExportDefinitionTypeRepository) {
        this.systemExportDefinitionTypeRepository = systemExportDefinitionTypeRepository;
    }

}