package com.system.bean;

import com.system.bean.base.BaseBeanIntegrationTest;
import org.junit.Test;

import static com.system.util.collection.CollectionUtils.size;
import static com.system.util.validation.ValidationUtils.assertGreaterThan;

/**
 * The <class>SystemBeanTests</class> tests
 * system beans
 *
 * @author Andrew
 */
public class SystemBeanTests extends BaseBeanIntegrationTest {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                                                Tests                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Test
    public void testSchemaTableSize() throws Exception {
        assertGreaterThan(size(systemBeanDefinitionTypeRepository.findAll()), 0);
    }
}