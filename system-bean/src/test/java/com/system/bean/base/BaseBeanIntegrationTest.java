package com.system.bean.base;


import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.db.migration.base.BaseIntegrationTest;
import com.system.db.repository.base.named.NamedEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The <class>BaseBeanIntegrationTest</class> defines the
 * extended configuration for bean specific integration tests
 * including reused properties.
 *
 * @author Andrew
 */
public abstract class BaseBeanIntegrationTest extends BaseIntegrationTest {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    protected NamedEntityRepository<SystemBeanDefinitionType> systemBeanDefinitionTypeRepository;
}