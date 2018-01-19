package com.system.db.schema.table.column;


import com.system.db.repository.base.entity.EntityRepository;
import com.system.db.repository.base.named.NamedEntityRepository;

import java.util.List;

/**
 * The <class>SchemaTableColumnRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see EntityRepository
 */
public interface SchemaTableColumnRepository extends NamedEntityRepository<SchemaTableColumn> {

    /**
     * Find the SchemaTableColumn by name for
     * a specific table
     *
     * @param schemaTableName
     * @param name
     * @return
     */
    public SchemaTableColumn findBySchemaTableNameAndName(String schemaTableName, String name);

    /**
     * Find the SchemaTableColumns for
     * a specific table
     *
     * @param schemaTableName
     * @return
     */
    public List<SchemaTableColumn> findBySchemaTableName(String schemaTableName);
}