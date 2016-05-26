package com.system.security.user;

import com.system.db.repository.BaseEntityRepository;

/**
 * The <class>SystemSecurityUserRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see BaseEntityRepository
 */
public interface SystemSecurityUserRepository extends BaseEntityRepository<SystemSecurityUser> {

    /**
     * Find the SecurityUser by username
     *
     * @param username
     * @return
     */
    public SystemSecurityUser findByUsername(String username);
}