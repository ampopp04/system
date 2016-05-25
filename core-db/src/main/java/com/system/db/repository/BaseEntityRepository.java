package com.system.db.repository;

import com.system.db.entity.identity.EntityIdentity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The <interface>BaseEntityRepository</interface> defines the
 * basic entity repository used by system
 * repositories to access entities from the database
 * <p>
 *
 * @author Andrew
 * @see EntityIdentity
 */
@NoRepositoryBean
public interface BaseEntityRepository<T extends EntityIdentity> extends CrudRepository<T, T> {
}
