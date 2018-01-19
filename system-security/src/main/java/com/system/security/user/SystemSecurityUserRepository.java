package com.system.security.user;

import com.system.db.repository.base.entity.EntityRepository;
import com.system.db.repository.base.named.NamedEntityRepository;

/**
 * The <class>SystemSecurityUserRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see EntityRepository
 */
public interface SystemSecurityUserRepository extends NamedEntityRepository<SystemSecurityUser> {

    /**
     * Find the SecurityUser by username
     *
     * @param username
     * @return
     */
    public SystemSecurityUser findByUsername(String username);
}