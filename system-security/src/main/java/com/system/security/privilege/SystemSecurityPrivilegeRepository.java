package com.system.security.privilege;

import com.system.db.repository.BaseEntityRepository;

/**
 * The <class>SystemSecurityPrivilegeRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see BaseEntityRepository
 */
public interface SystemSecurityPrivilegeRepository extends BaseEntityRepository<SystemSecurityPrivilege> {
    /**
     * Find security privilege by name
     *
     * @param name
     * @return
     */
    public SystemSecurityPrivilege findByName(String name);

}