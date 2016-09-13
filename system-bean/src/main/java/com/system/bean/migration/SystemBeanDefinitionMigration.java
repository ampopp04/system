package com.system.bean.migration;

import com.system.bean.definition.SystemBeanDefinition;
import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.bean.type.SystemBeanType;
import com.system.bean.variable.definition.SystemBeanVariableDefinition;
import com.system.db.migration.data.BaseDataMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.table.SchemaTable;
import com.system.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The <class>SystemBeanDefinitionMigration</class> is an easy to use
 * manual assist for creating Bean definitions.
 *
 * @author Andrew
 * @see SystemBeanDefinition
 * @see SystemBeanType
 * @see SystemBeanVariableDefinition
 */
public abstract class SystemBeanDefinitionMigration extends BaseDataMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    protected NamedEntityRepository<SystemBeanDefinitionType> systemBeanDefinitionTypeRepository;

    @Autowired
    protected NamedEntityRepository<SystemBeanVariableDefinition> systemBeanVariableDefinitionRepository;

    @Autowired
    protected NamedEntityRepository<SystemBeanDefinition> systemBeanDefinitionRepository;

    @Autowired
    protected NamedEntityRepository<SystemBeanType> systemBeanTypeRepository;

    @Autowired
    protected NamedEntityRepository<SchemaTable> schemaTableRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    protected abstract SystemBeanDefinition retrieveSystemBeanDefinition();

    protected abstract SystemBeanType retrieveSystemBeanType();

    protected abstract List<SystemBeanVariableDefinition> retrieveSystemBeanVariableDefinitionList();

    @Override
    protected void insertData() {
        SystemBeanDefinition systemBeanDefinition = createOrGetBeanDefinition();
        SystemBeanType systemBeanType = createOrGetBeanType(systemBeanDefinition);
        createOrGetBeanVariableDefinitions(systemBeanType);
    }

    private SystemBeanDefinition createOrGetBeanDefinition() {
        SystemBeanDefinition systemBeanDefinition = retrieveSystemBeanDefinition();
        systemBeanDefinitionRepository.save(systemBeanDefinition);
        return systemBeanDefinition;
    }

    private SystemBeanType createOrGetBeanType(SystemBeanDefinition systemBeanDefinition) {
        SystemBeanType systemBeanType = retrieveSystemBeanType();
        systemBeanType.setSystemBeanDefinition(systemBeanDefinition);
        this.systemBeanTypeRepository.save(systemBeanType);

        return systemBeanType;
    }

    private List<SystemBeanVariableDefinition> createOrGetBeanVariableDefinitions(SystemBeanType systemBeanType) {
        List<SystemBeanVariableDefinition> variableDefinitionList = retrieveSystemBeanVariableDefinitionList();
        CollectionUtils.iterate(CollectionUtils.iterable(variableDefinitionList), (variableDefinition) -> variableDefinition.setSystemBeanType(systemBeanType));
        systemBeanVariableDefinitionRepository.save(variableDefinitionList);

        return variableDefinitionList;
    }

    protected SystemBeanDefinition getSystemBeanDefinitionByName(String name) {
        return systemBeanDefinitionRepository.findByName(name);
    }
}