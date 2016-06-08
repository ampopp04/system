package com.system.db.repository.base.identity;

import com.system.db.entity.identity.EntityIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The <interface>EntityIdentityRepository</interface> defines the
 * basic entity repository used by system
 * repositories to access entities from the database
 * <p>
 *
 * @author Andrew
 * @see EntityIdentity
 */
@NoRepositoryBean
public interface EntityIdentityRepository<T extends EntityIdentity> extends JpaRepository<T, T>, JpaSpecificationExecutor<T> {
}
