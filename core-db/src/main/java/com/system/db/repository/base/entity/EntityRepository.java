package com.system.db.repository.base.entity;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.identity.EntityIdentityRepository;
import org.springframework.data.repository.NoRepositoryBean;

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
public interface EntityRepository<T extends BaseEntity> extends EntityIdentityRepository<T> {

    /**
     * Find entity by id
     *
     * @param id
     * @return
     */
    public T findById(Integer id);
}