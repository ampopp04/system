package db.migration;

import com.system.db.migration.SystemTable;
import com.system.db.migration.table.TableCreationMigration;


public class V1__initial_schema extends TableCreationMigration {
    protected Class[] getEntityClasses() {
        return new Class[]{SystemTable.class};
    }
}