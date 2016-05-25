package com.system.db.migration.base;


import com.system.db.migration.schema.SchemaVersionRepository;
import com.system.db.migration.schema.datatype.SystemDataTypeRepository;
import com.system.db.migration.schema.table.SystemTableRepository;
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
    protected SystemTableRepository systemTableRepository;

    @Autowired
    protected SchemaVersionRepository schemaVersionRepository;

    @Autowired
    protected SystemDataTypeRepository systemDataTypeRepository;

}
