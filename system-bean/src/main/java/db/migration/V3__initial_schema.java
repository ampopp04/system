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
import com.system.db.repository.base.named.NamedEntityRepository;
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

    @Autowired
    private NamedEntityRepository<SystemBeanModifierType> systemBeanModifierTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemBeanDefinition> systemBeanDefinitionRepository;

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
        systemBeanModifierTypeRepository.save(getSystemBeanModifierTypeList());
        systemBeanDefinitionRepository.save(getSystemBeanDefinitionList());
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

    private List<SystemBeanModifierType> getSystemBeanModifierTypeList() {
        return asList(
                getSystemBeanModifierType("public", "Java keyword used in a method or variable declaration. It signifies that the method or variable can be accessed by elements residing in other classes."),
                getSystemBeanModifierType("private", "A Java keyword used in a method or variable declaration. It signifies that the method or variable can only be accessed by other elements of its class."),
                getSystemBeanModifierType("final", "You define an entity once and cannot change it or derive from it later. More specifically: a final class cannot be subclassed, a final method cannot be overridden and a final variable cannot change from its initialized value."),
                getSystemBeanModifierType("static", "A Java keyword used to define a variable as a class variable. Classes maintain one copy of class variables regardless of how many instances exist of that class. static can also be used to define a method as a class method.")
        );
    }

    private SystemBeanModifierType getSystemBeanModifierType(String name, String description) {
        SystemBeanModifierType entity = new SystemBeanModifierType();
        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }

    private List<SystemBeanDefinition> getSystemBeanDefinitionList() {
        return asList(
                getSystemBeanDefinition("Inversion Bean", "An inversion bean is a broad definition for various types of beans that are dynamically injected into the inversion context for use by the application.")
        );
    }

    private SystemBeanDefinition getSystemBeanDefinition(String name, String description) {
        SystemBeanDefinition entity = new SystemBeanDefinition();
        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }
}