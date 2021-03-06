package com.system.security.base;


import com.system.db.migration.base.BaseIntegrationTest;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.security.privilege.SystemSecurityPrivilege;
import com.system.security.role.SystemSecurityRole;
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
    protected NamedEntityRepository<SystemSecurityPrivilege> systemSecurityPrivilegeRepository;

    @Autowired
    protected NamedEntityRepository<SystemSecurityRole> systemSecurityRoleRepository;

    @Autowired
    protected SystemSecurityUserRepository systemSecurityUserRepository;
}
