/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.schema.datatype;


import com.system.db.repository.base.entity.EntityRepository;

/**
 * The <class>SchemaDataTypeRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see EntityRepository
 */
public interface SchemaDataTypeRepository extends EntityRepository<SchemaDataType, Integer> {

    /**
     * Find SchemaDataType represented by a
     * given fully qualified class name java type
     *
     * @param javaType
     * @return
     */
    public SchemaDataType findByJavaType(String javaType);
}