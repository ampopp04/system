package com.system.db.repository.base.identity;

import com.system.db.entity.identity.EntityIdentity;
import com.system.db.repository.base.entity.BaseEntityRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

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
public interface EntityIdentityRepository<T extends EntityIdentity, ID extends Serializable> extends BaseEntityRepository<T, ID> {
}