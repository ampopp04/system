package com.system.db.schema.table.column;


import com.system.db.repository.BaseEntityRepository;

/**
 * The <class>SchemaTableColumnRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see BaseEntityRepository
 */
public interface SchemaTableColumnRepository extends BaseEntityRepository<SchemaTableColumn> {

    /**
     * Find the SchemaTableColumn by name for
     * a specific table
     *
     * @param schemaTableName
     * @param name
     * @return
     */
    public SchemaTableColumn findBySchemaTableNameAndName(String schemaTableName, String name);
}