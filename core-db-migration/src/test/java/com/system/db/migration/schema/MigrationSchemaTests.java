package com.system.db.migration.schema;


import com.system.db.migration.base.BaseDbMigrationIntegrationTest;
import org.junit.Test;

import static com.system.util.collection.CollectionUtils.size;
import static com.system.util.validation.ValidationUtils.assertEqualTo;
import static com.system.util.validation.ValidationUtils.assertGreaterThan;

/**
 * The <class>MigrationSchemaTests</class> defines the initial
 * database migration schema tests.
 *
 * @author Andrew
 */
public class MigrationSchemaTests extends BaseDbMigrationIntegrationTest {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                                                Tests                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Test
    public void testSchemaTableSize() throws Exception {
        assertEqualTo(size(systemTableRepository.findAll()), 0);
        assertGreaterThan(size(this.schemaVersionRepository.findAll()), 0);
        assertGreaterThan(size(this.systemDataTypeRepository.findAll()), 10);
    }
}
