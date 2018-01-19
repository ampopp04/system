package com.system.db.migration.schema;


import com.system.db.migration.base.BaseDbMigrationIntegrationTest;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.data.domain.Pageable;

/**
 * The <class>MigrationSchemaTests</class> defines the initial
 * database migration schema tests.
 *
 * @author Andrew
 */

@FlywayTest(invokeBaselineDB = true, invokeCleanDB = false, locationsForMigrate = {"classpath:db/migration"})
public class MigrationSchemaTests extends BaseDbMigrationIntegrationTest {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    ////////                                                                Tests                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Test
    @FlywayTest(invokeBaselineDB = true, invokeCleanDB = false, locationsForMigrate = {"classpath:db/migration"})
    public void testAdvancedNullPropertySearch() throws Exception {
        schemaTableColumnRepository.findAll(
                "[{\"operator\":\"\",\"value\":\"nam\",\"property\":\"null-all-property\"}]",
                2,
                null,
                Pageable.unpaged());
    }
}
