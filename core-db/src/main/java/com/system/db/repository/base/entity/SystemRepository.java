package com.system.db.repository.base.entity;

import com.system.db.entity.base.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * The <class>SystemRepository</class> defines a base repository
 * to be used to access the majority of the database access methods
 * for a given entity type
 *
 * @author Andrew
 */
@NoRepositoryBean
public interface SystemRepository<T extends BaseEntity, ID extends Serializable> extends EntityRepository<T, ID> {
}