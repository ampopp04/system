package com.system.security.schema;


import com.system.security.base.BaseSecurityIntegrationTest;
import org.junit.Test;

import static com.system.util.collection.CollectionUtils.size;
import static com.system.util.validation.ValidationUtils.assertGreaterThan;

/**
 * The <class>MigrationSchemaTests</class> defines the initial
 * database migration schema tests.
 *
 * @author Andrew
 */
public class SecuritySchemaTests extends BaseSecurityIntegrationTest {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                                                Tests                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Test
    public void testSchemaTableSize() throws Exception {
        assertGreaterThan(size(systemSecurityPrivilegeRepository.findAll()), 0);
        assertGreaterThan(size(systemSecurityRoleRepository.findAll()), 0);
        assertGreaterThan(size(systemSecurityUserRepository.findAll()), 0);
    }
}
