package com.system.db.repository.base.entity;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.identity.EntityIdentityRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * The <interface>EntityRepository</interface> defines the
 * basic entity repository used by system
 * repositories to access entities from the database
 * <p>
 *
 * @author Andrew
 * @see BaseEntity
 */
@NoRepositoryBean
public interface EntityRepository<T extends BaseEntity, ID extends Serializable> extends EntityIdentityRepository<T, ID> {
}