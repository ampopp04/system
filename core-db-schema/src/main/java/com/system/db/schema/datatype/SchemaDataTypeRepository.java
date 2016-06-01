package com.system.db.schema.datatype;


import com.system.db.repository.BaseEntityRepository;

/**
 * The <class>SchemaDataTypeRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see BaseEntityRepository
 */
public interface SchemaDataTypeRepository extends BaseEntityRepository<SchemaDataType> {

    /**
     * Find SchemaDataType represented by a
     * given fully qualified class name java type
     *
     * @param javaType
     * @return
     */
    public SchemaDataType findByJavaType(String javaType);
}