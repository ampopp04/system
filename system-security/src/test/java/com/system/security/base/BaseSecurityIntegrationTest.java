package com.system.security.base;


import com.system.db.migration.base.BaseIntegrationTest;
import com.system.security.privilege.SystemSecurityPrivilegeRepository;
import com.system.security.role.SystemSecurityRoleRepository;
import com.system.security.user.SystemSecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The <class>BaseSecurityIntegrationTest</class> defines the
 * extended configuration for security specific integration tests
 * including reused properties.
 *
 * @author Andrew
 */
public abstract class BaseSecurityIntegrationTest extends BaseIntegrationTest {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    protected SystemSecurityPrivilegeRepository systemSecurityPrivilegeRepository;

    @Autowired
    protected SystemSecurityRoleRepository systemSecurityRoleRepository;

    @Autowired
    protected SystemSecurityUserRepository systemSecurityUserRepository;
}
