package com.system.security.role;

import com.system.db.repository.BaseEntityRepository;

/**
 * The <class>SystemSecurityRoleRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see BaseEntityRepository
 */
public interface SystemSecurityRoleRepository extends BaseEntityRepository<SystemSecurityRole> {

    /**
     * Get a security role by name
     *
     * @param name
     * @return
     */
    public SystemSecurityRole findByName(String name);

}