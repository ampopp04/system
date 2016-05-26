package com.system.db.migration.base;


import com.system.db.schema.datatype.SchemaDataTypeRepository;
import com.system.db.schema.table.SchemaTableRepository;
import com.system.db.schema.table.column.SchemaTableColumnRepository;
import com.system.db.schema.table.column.relationship.SchemaTableColumnRelationshipRepository;
import com.system.db.schema.version.SchemaVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The <class>BaseDbMigrationIntegrationTest</class> defines the
 * extended configuration for Db Migration specific integration tests
 * including reused properties.
 *
 * @author Andrew
 */
public abstract class BaseDbMigrationIntegrationTest extends BaseIntegrationTest {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    protected SchemaDataTypeRepository schemaDataTypeRepository;

    @Autowired
    protected SchemaTableRepository schemaTableRepository;

    @Autowired
    protected SchemaTableColumnRepository schemaTableColumnRepository;

    @Autowired
    protected SchemaTableColumnRelationshipRepository schemaTableColumnRelationshipRepository;

    @Autowired
    protected SchemaVersionRepository schemaVersionRepository;
}
