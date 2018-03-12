/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.repository.base.named;

import com.system.db.entity.named.NamedEntity;
import com.system.db.repository.base.entity.SystemRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The <interface>NamedEntityRepository</interface> defines the
 * basic entity repository used by system
 * repositories to access named entities from the database
 * <p>
 *
 * @author Andrew
 * @see NamedEntity
 */
@NoRepositoryBean
public interface NamedEntityRepository<T extends NamedEntity> extends SystemRepository<T, Integer> {

    /**
     * Find entity by name
     *
     * @param name
     * @return
     */
    public T findByName(String name);
}
