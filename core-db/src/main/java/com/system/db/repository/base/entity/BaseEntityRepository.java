/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.repository.base.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The <interface>BaseEntityRepository</interface> defines
 * our main repository with an underlying implementation for the system framework.
 * <p>
 * It also allows overriding or adding methods to all repositories for database access
 *
 * @author Andrew
 */
@NoRepositoryBean
public interface BaseEntityRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * Add search feature to all repositories.
     * This allows a url encoded with a filter in
     * json form.
     * <p>
     * Ex: filter:[{"operator":"like","value":"asd","property":"description"}
     *
     * @param searchFilter - what we would like to search for based on the operations specified in the search criteria array
     * @param pageable     - this enables sorting and paging
     * @return - the paged results sorted and filtered based on the search criteria
     */
    public Page<T> findAll(String searchFilter, Integer searchDepth, String[] extraSearchParams, Pageable pageable);

}
