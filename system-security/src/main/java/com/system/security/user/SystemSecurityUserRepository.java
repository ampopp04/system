package com.system.security.user;

import com.system.db.repository.base.entity.EntityRepository;

/**
 * The <class>SystemSecurityUserRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see EntityRepository
 */
public interface SystemSecurityUserRepository extends EntityRepository<SystemSecurityUser> {

    /**
     * Find the SecurityUser by username
     *
     * @param username
     * @return
     */
    public SystemSecurityUser findByUsername(String username);
}