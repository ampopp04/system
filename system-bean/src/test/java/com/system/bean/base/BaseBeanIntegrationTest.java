package com.system.bean.base;


import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.db.migration.base.BaseIntegrationTest;
import com.system.db.repository.base.entity.SystemRepository;
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
    protected SystemRepository<SystemBeanDefinitionType> systemBeanDefinitionTypeRepository;
}