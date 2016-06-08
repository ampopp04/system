package db.migration;

import com.system.bean.SystemBean;
import com.system.bean.definition.SystemBeanDefinition;
import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.bean.modifier.type.SystemBeanModifierType;
import com.system.bean.type.SystemBeanType;
import com.system.bean.variable.SystemBeanVariable;
import com.system.bean.variable.definition.SystemBeanVariableDefinition;
import com.system.bean.variable.definition.modifier.type.SystemBeanVariableDefinitionModifierType;
import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.entity.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V3__initial_schema</class> defines the initial
 * bean schema creation.
 * <p>
 * This defines various tables that manage system beans.
 *
 * @author Andrew
 */
public class V3__initial_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private SystemRepository<SystemBeanDefinitionType> systemBeanDefinitionTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return asList(
                SystemBeanDefinitionType.class, SystemBeanDefinition.class, SystemBeanType.class,
                SystemBeanModifierType.class,
                SystemBeanVariableDefinition.class, SystemBeanVariableDefinitionModifierType.class,
                SystemBean.class, SystemBeanVariable.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        systemBeanDefinitionTypeRepository.save(getSystemBeanDefinitionTypeList());
    }

    public List<SystemBeanDefinitionType> getSystemBeanDefinitionTypeList() {
        return asList(
                createSystemBeanDefinitionType("Interface", "This definition type represents an interface."),
                createSystemBeanDefinitionType("Annotation", "This definition type represents an annotation."),
                createSystemBeanDefinitionType("Enum", "This definition type represents an enum."),
                createSystemBeanDefinitionType("Class", "This definition type represents a class.")
        );
    }

    private SystemBeanDefinitionType createSystemBeanDefinitionType(String name, String description) {
        SystemBeanDefinitionType entity = new SystemBeanDefinitionType();
        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }
}